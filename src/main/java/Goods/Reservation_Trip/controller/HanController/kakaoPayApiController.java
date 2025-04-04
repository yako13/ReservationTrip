package Goods.Reservation_Trip.controller.HanController;

import Goods.Reservation_Trip.service.HanService.KakaoPayService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequiredArgsConstructor
public class kakaoPayApiController {

    private final KakaoPayService kakaoPayService;



    @PostMapping("/pay")
    @ResponseBody
    public String pay(@RequestParam Long packagePk, @RequestParam BigDecimal totalPay, HttpSession session, HttpServletRequest request) {

        int pay = totalPay.intValue();

        String redirectUrl = kakaoPayService.kakaoPayReady(packagePk, session,request,pay);


        return redirectUrl; // redirect 하지 않고 URL만 반환
    }

}
