package Goods.Reservation_Trip.controller;

import Goods.Reservation_Trip.service.reservation.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ReservationCancellationController {

    private final NotificationService notificationService;

    @PostMapping("/{reservationCode}/cancel")
    public String cancelReservation(@PathVariable String reservationCode){
        notificationService.sendCancelNotification(reservationCode);
        return "예약 취소요청이 완료되었습니다.";
    }
}
