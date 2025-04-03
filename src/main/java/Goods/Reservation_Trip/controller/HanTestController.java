package Goods.Reservation_Trip.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HanTestController {


    @GetMapping("/test10")
    public String test10Go() {


        return "testPage";
    }

    @GetMapping("/")
    public String MainPageGo() {



        return "mainPage";
    }

    //예약 페이지
    @GetMapping("/reservation")
    public String reservationGo() {



        return "reservation/reservation";
    }

    //예약 완료 페이지
    @GetMapping("/reservation/Complete")
    public String reservationCompleteGo() {



        return "reservation/reservationComplete";
    }

    //찜 목록 페이지
    @GetMapping("/dib")
    public String dibGo() {


        return "dib";
    }

    //찜 목록 페이지
    @GetMapping("member/reservationDetail")
    public String reservationDetailGo() {


        return "reservation/reservationDetail";
    }

    //패키지 상세 페이지
    @GetMapping("package/details")
    public String packageDetailsGo() {


        return "package/packageDetails";
    }







}
