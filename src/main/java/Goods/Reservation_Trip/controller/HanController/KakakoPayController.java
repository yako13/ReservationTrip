package Goods.Reservation_Trip.controller.HanController;


import Goods.Reservation_Trip.dto.HanDto.KakaoPay;
import Goods.Reservation_Trip.service.HanService.KakaoPayService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class KakakoPayController {

    private final KakaoPayService kakaoPayService;

    //카카오 페이 결제 승인 요청 및 응답
    @GetMapping("/pay/approve")
    public String kakaoPayApprove(@RequestParam("pg_token") String pg_token,
                                  HttpSession session,
                                  Model model, HttpServletRequest request) {

        //서비스를 통해 카카오 페이 응답 값을 받아온다
        KakaoPay kakaoPay = kakaoPayService.approvePayment(pg_token, session, request);

        // 응답값을 model에 담아서 popup 페이지로 전달
        model.addAttribute("kakaoPay", kakaoPay);

        return "reservation/reservationPopUp"; // 팝업에서 부모창에 값 전달하는 페이지
    }

    // 결제 내역 페이지


    // 실패/취소 페이지도 필요하면 추가 가능
    @GetMapping("/pay/fail")
    public String payFail() {
        return "fail";
    }

    @GetMapping("/pay/cancel")
    public String payCancel() {
        return "cancel";
    }
}
