package Goods.Reservation_Trip.controller.HanController;

import Goods.Reservation_Trip.dto.HanDto.PackPageDto;
import Goods.Reservation_Trip.repository.HanPart.HanDibRepository;
import Goods.Reservation_Trip.repository.MemberRepository;
import Goods.Reservation_Trip.repository.aPackage.PackageRepository;
import Goods.Reservation_Trip.service.HanService.HanDibService;
import Goods.Reservation_Trip.service.HanService.HanMemberService;
import Goods.Reservation_Trip.service.HanService.HanPackageService;
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

    private final HanMemberService hanMemberService;
    private final MemberRepository memberRepository;
    private final HanDibRepository hanDibRepository;
    private final PackageRepository packageRepository;
    private final HanPackageService hanPackageService;
    private final HanDibService hanDibService;

    //패키지 상세 페이지
    @GetMapping("/package/{id}")
    public String packageDetailsGo(Model model, HttpServletRequest request, @PathVariable("id") Long id, RedirectAttributes rttr) {

        PackPageDto packPageDto = hanPackageService.productDetailGo(request, id);

        if (packPageDto == null) {

            log.error("서비스에서 에러 발생");

            rttr.addFlashAttribute("data","죄송합니다 에러가 발생했습니다. 다시 시도해주세요");

            return "redirect:/";

        }

        //패키지 및 여행 정보 보내기
        model.addAttribute("packPageDto", packPageDto);

        //찜을 했는지 체크하는 서비스
        boolean isLiked = hanDibService.packageDibCheck(request,id);

        //찜 정보 보내기
        model.addAttribute("isLiked", isLiked);

        //리뷰 목록 보내기


        return "package/packageDetails";
    }


}
