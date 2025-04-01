package Goods.Reservation_Trip.service.reservation;

import Goods.Reservation_Trip.dto.reservation.req.NotificationMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final SimpMessagingTemplate messagingTemplate;

    public void sendCancelNotification(String reservationCode){

        NotificationMessage message = new NotificationMessage("예약번호"+reservationCode+"의 예약취소 요청이 있습니다. ","cancel");
        messagingTemplate.convertAndSend("/topic/admin",message);
    }
}
