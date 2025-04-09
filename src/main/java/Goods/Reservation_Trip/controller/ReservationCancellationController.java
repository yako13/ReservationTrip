package Goods.Reservation_Trip.controller;

import Goods.Reservation_Trip.dto.member.res.MemberResponseDto;
import Goods.Reservation_Trip.service.member.MemberService;
import Goods.Reservation_Trip.service.reservation.ReservationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ReservationCancellationController {

    private final ReservationService reservationService;

    private final MemberService memberService;

    @GetMapping("/member/{reservationCode}/cancel")
    public String cancelReservation(@PathVariable String reservationCode, HttpServletRequest request){
        MemberResponseDto memberResponseDto = memberService.getMember(request);
        reservationService.sendCancelNotification(reservationCode,memberResponseDto.getId());
        return "예약 취소요청이 완료되었습니다.";
    }
}
