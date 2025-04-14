package Goods.Reservation_Trip.controller;

import Goods.Reservation_Trip.dto.member.res.MemberResponseDto;
import Goods.Reservation_Trip.service.member.MemberService;
import Goods.Reservation_Trip.service.reservation.ReservationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class ReservationCancellationController {

    private final ReservationService reservationService;

    private final MemberService memberService;


//    @GetMapping("/member/{reservationCode}/cancel")
//    public String cancelReservation(@PathVariable String reservationCode, HttpServletRequest request){
//        MemberResponseDto memberResponseDto = memberService.getMember(request);
//        reservationService.sendCancelNotification(reservationCode,memberResponseDto.getId());
//        return "예약 취소요청이 완료되었습니다.";
//    }

    @PostMapping("/member/{reservationCode}/cancel")
    public String cancelReservation(@PathVariable String reservationCode, HttpServletRequest request, RedirectAttributes rttr) {
        MemberResponseDto memberResponseDto = memberService.getMember(request);
        reservationService.sendCancelNotification(reservationCode, memberResponseDto.getId());
        rttr.addFlashAttribute("data","예약 취소요청이 완료되었습니다.");
        return "redirect:/member/reservation/list";
    }
}
