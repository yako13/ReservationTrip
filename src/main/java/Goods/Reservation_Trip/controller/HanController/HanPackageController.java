package Goods.Reservation_Trip.controller.HanController;

import Goods.Reservation_Trip.dto.HanDto.*;
import Goods.Reservation_Trip.entity.Airline;
import Goods.Reservation_Trip.entity.Airport;
import Goods.Reservation_Trip.entity.Package;
import Goods.Reservation_Trip.service.HanService.HanDibService;
import Goods.Reservation_Trip.service.HanService.HanHeaderService;
import Goods.Reservation_Trip.service.HanService.HanPackageService;
import Goods.Reservation_Trip.service.HanService.HanReviewService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HanPackageController {

    private final HanPackageService hanPackageService;
    private final HanDibService hanDibService;
    private final HanReviewService hanReviewService;
    private final HanHeaderService hanHeaderService;

    //패키지 전체 목록
    @GetMapping("/package/category3")
    public String packageListGo(@RequestParam(defaultValue = "10") int size,
                                @RequestParam(defaultValue = "0") int page,
                                Model model, RedirectAttributes rttr,
                                HttpServletRequest request) {

        //헤더 카테고리랑 로그인 상태 및 유저이름 보내주는 서비스
        HeaderDto headerDto = hanHeaderService.HeaderCategoryAndMember(request);

        model.addAttribute("headerDto", headerDto);


        //페이지 네이션 해주는 서비스 (패키지 상태가 AVAILABLE 인것만)
        Page<PackPageListDto> packPageListDto = hanPackageService.packageListGo(page, size);


        model.addAttribute("packPageListDto", packPageListDto);

        int current = packPageListDto.getNumber();
        int total = packPageListDto.getTotalPages();

        int start = Math.max(0, current - 5);
        int end = Math.min(total - 1, start + 9); // 최대 10개만

        // 만약 end - start < 9라면 start 보정
        if (end - start < 9) {
            start = Math.max(0, end - 9);
        }

        model.addAttribute("startPage", start);
        model.addAttribute("endPage", end);

        long totalCount = packPageListDto.getTotalElements();

        model.addAttribute("totalCount", totalCount);

//        return "package/testp";
        return "package/package-category2";
    }

    //패키지 상세 페이지
    @GetMapping("/package/details/{id}")
    public String packageDetailsGo(Model model, HttpServletRequest request, @PathVariable("id") Long id, RedirectAttributes rttr) {

        //헤더 카테고리랑 로그인 상태 및 유저이름 보내주는 서비스
        HeaderDto headerDto = hanHeaderService.HeaderCategoryAndMember(request);

        model.addAttribute("headerDto", headerDto);

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


    @GetMapping("/packages")
    public String listPackages(@RequestParam(defaultValue = "10") int size,
                               @RequestParam(defaultValue = "0") int page,
                               @ModelAttribute PackCategoryDto packCategoryDto, Model model,
                               HttpServletRequest request,
                               RedirectAttributes rttr) {

        //헤더 카테고리랑 로그인 상태 및 유저이름 보내주는 서비스
        HeaderDto headerDto = hanHeaderService.HeaderCategoryAndMember(request);

        model.addAttribute("headerDto", headerDto);


        log.info("packages 컨트롤러 작동");

        Page<PackPageListDto> packPageListDto =
                hanPackageService.filterPackagesWithPaging(packCategoryDto, page, size);

        int current = packPageListDto.getNumber();
        int total = packPageListDto.getTotalPages();

        int start = Math.max(0, current - 5);
        int end = Math.min(total - 1, start + 9); // 최대 10개만

        // 만약 end - start < 9라면 start 보정
        if (end - start < 9) {
            start = Math.max(0, end - 9);
        }

        model.addAttribute("startPage", start);
        model.addAttribute("endPage", end);

        long totalCount = packPageListDto.getTotalElements();

        model.addAttribute("totalCount", totalCount);


        //카테고리 이름을 담아준다
        if (packCategoryDto.getCategoryId() != null && packCategoryDto.getCategoryName() != null) {

            model.addAttribute("category", packCategoryDto.getCategoryName());

        } else {

            model.addAttribute("category", "전체상품");

        }

        //공항 정보를 가져오는 서비스
        List<Airport> airportList = hanPackageService.AirportCategory();

        if (airportList == null) {

            log.error("서비스에서 에러 발생");

            rttr.addFlashAttribute("data", "죄송합니다 에러가 발생했습니다. 다시 시도해주세요");

            return "redirect:/";
        }

        model.addAttribute("airportList", airportList);

        //선택한 공항 정보 가져오는 서비스
        String selectAirport = hanPackageService.AirportCategoryName(packCategoryDto);

        model.addAttribute("selectAirport", selectAirport);


        model.addAttribute("packPageListDto", packPageListDto);
        return "package/package-category2"; // 패키지 리스트를 보여주는 페이지


    }


}
