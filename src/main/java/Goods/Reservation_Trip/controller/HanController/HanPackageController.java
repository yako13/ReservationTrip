package Goods.Reservation_Trip.controller.HanController;

import Goods.Reservation_Trip.dto.HanDto.PackPageDto;
import Goods.Reservation_Trip.dto.HanDto.PackReviewDto;
import Goods.Reservation_Trip.service.HanService.HanDibService;
import Goods.Reservation_Trip.service.HanService.HanPackageService;
import Goods.Reservation_Trip.service.HanService.HanReviewService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HanPackageController {

    private final HanPackageService hanPackageService;
    private final HanDibService hanDibService;
    private final HanReviewService hanReviewService;

    //패키지 상세 페이지
    @GetMapping("/package/{id}")
    public String packageDetailsGo(Model model, HttpServletRequest request, @PathVariable("id") Long id, RedirectAttributes rttr) {

        if (id == null) {

            log.error("id가 null입니다");

            rttr.addFlashAttribute("data", "잘못된 접근입니다");

            return "redirect:/";

        }

        PackPageDto packPageDto = hanPackageService.productDetailGo(request, id);

        if (packPageDto == null) {

            log.error("서비스에서 에러 발생");

            rttr.addFlashAttribute("data", "죄송합니다 에러가 발생했습니다. 다시 시도해주세요");

            return "redirect:/";

        }

        //패키지 및 여행 정보 보내기
        model.addAttribute("packPageDto", packPageDto);

        //찜을 했는지 체크하는 서비스
        boolean isLiked = hanDibService.packageDibCheck(request, id);

        //찜 정보 보내기
        model.addAttribute("isLiked", isLiked);

        //리뷰 목록 보내기
        PackReviewDto packReviewDto = hanReviewService.packReviewGo(id);

        model.addAttribute("packReviewDto", packReviewDto);


        return "package/packageDetails";
    }

    @GetMapping("/packageCategory2")
    public String packageCa(Model model,  RedirectAttributes rttr) {



        return "package/package-category2";
    }


}
