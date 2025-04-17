package Goods.Reservation_Trip.controller;

import Goods.Reservation_Trip.dto.HanDto.CityDto;
import Goods.Reservation_Trip.dto.HanDto.HeaderDto;
import Goods.Reservation_Trip.dto.HanDto.PackPageListDto;
import Goods.Reservation_Trip.entity.Airport;
import Goods.Reservation_Trip.service.HanService.HanHeaderService;
import Goods.Reservation_Trip.service.HanService.HanPackageService;
import Goods.Reservation_Trip.service.member.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;


@Controller
@RequiredArgsConstructor
public class HanTestController {

    private final MemberService memberService;
    private final HanHeaderService hanHeaderService;
    private final HanPackageService hanPackageService;

    @GetMapping("/test10")
    public String test10Go() {


        return "testPage";
    }

    @GetMapping("/")
    public String MainPageGo(HttpServletRequest request, Model model) {

        //헤더 카테고리랑 로그인 상태 및 유저이름 보내주는 서비스
        HeaderDto headerDto = hanHeaderService.HeaderCategoryAndMember(request);

        model.addAttribute("headerDto",headerDto);

        //출발지 정보 (공항 )
        List<Airport> airportList = hanPackageService.airportCategory();

        model.addAttribute("airportList", airportList);

        //도착지(도시) 정보
        List<CityDto> cityList = hanPackageService.hanCityAll();

        model.addAttribute("cityList", cityList);

        //베스트 4개
        List<PackPageListDto> BestTop4 = hanPackageService.packBestTop4();

        model.addAttribute("BestTop4", BestTop4);


        //리뷰 평점 4개
        List<PackPageListDto> ReviewTop4 = hanPackageService.packReviewTop4();

        model.addAttribute("ReviewTop4", ReviewTop4);


        //신상품 4개
        List<PackPageListDto> NewTop4 = hanPackageService.packNewTop4();

        model.addAttribute("NewTop4", NewTop4);



        return "mainPage";
    }


}
