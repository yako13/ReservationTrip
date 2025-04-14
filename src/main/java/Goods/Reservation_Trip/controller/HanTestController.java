package Goods.Reservation_Trip.controller;

import Goods.Reservation_Trip.dto.HanDto.HeaderDto;
import Goods.Reservation_Trip.dto.member.res.MemberResponseDto;
import Goods.Reservation_Trip.service.HanService.HanHeaderService;
import Goods.Reservation_Trip.service.HanService.HanMemberService;
import Goods.Reservation_Trip.service.member.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
@RequiredArgsConstructor
public class HanTestController {

    private final MemberService memberService;
    private final HanHeaderService hanHeaderService;

    @GetMapping("/test10")
    public String test10Go() {


        return "testPage";
    }

    @GetMapping("/")
    public String MainPageGo(HttpServletRequest request, Model model) {

        //헤더 카테고리랑 로그인 상태 및 유저이름 보내주는 서비스
        HeaderDto headerDto = hanHeaderService.HeaderCategoryAndMember(request);

        model.addAttribute("headerDto",headerDto);


        return "mainPage";
    }




}
