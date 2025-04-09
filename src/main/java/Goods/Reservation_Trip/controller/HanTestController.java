package Goods.Reservation_Trip.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
@RequiredArgsConstructor
public class HanTestController {


    @GetMapping("/test10")
    public String test10Go() {


        return "testPage";
    }

    @GetMapping("/")
    public String MainPageGo() {


        return "mainPage";
    }




}
