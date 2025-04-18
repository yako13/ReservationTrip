package Goods.Reservation_Trip.service.reservation;

import Goods.Reservation_Trip.dto.reservation.req.NotificationMessage;
import Goods.Reservation_Trip.dto.review.res.NotificationApiDto;
import Goods.Reservation_Trip.dto.review.res.NotificationResponseDto;
import Goods.Reservation_Trip.entity.Member;
import Goods.Reservation_Trip.entity.Notification;
import Goods.Reservation_Trip.entity.Reservation;
import Goods.Reservation_Trip.enums.ReservationState;
import Goods.Reservation_Trip.repository.MemberRepository;
import Goods.Reservation_Trip.repository.NotificationRepository;
import Goods.Reservation_Trip.repository.ReservationRepository;
import Goods.Reservation_Trip.util.Formatter;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class NotificationService {

    private final ReservationRepository reservationRepository;

    private final NotificationRepository notificationRepository;

    private final SimpMessagingTemplate messagingTemplate;

    public void sendCancelNotification(String reservationCode, Member member) {
        Optional<Reservation> optionalReservation = reservationRepository.findByMemberIdAndCode(member.getId(), reservationCode);

        if (optionalReservation.isEmpty()) throw new RuntimeException("잘못된 접근");

        Reservation reservation = optionalReservation.get();

        reservation.setReservationState(ReservationState.REQUEST);

        reservationRepository.save(reservation);

        Notification notification = Notification.builder()
                .content("예약번호 " + reservationCode + "의 예약취소 요청이 있습니다.")
                .isRead(false)
                .member(member)
                .reservation(reservation)
                .build();

        Notification savedNotification = notificationRepository.save(notification);

        // WebSocket 전송용 DTO 만들기
        NotificationResponseDto dto = NotificationResponseDto.builder()
                .notificationPK(savedNotification.getId())
                .content(savedNotification.getContent())
                .read(savedNotification.isRead())
                .createdAt(Formatter.getLocalDate(savedNotification.getCreatedAt()))
                .reservationPK(reservation.getId())
                .build();

        // WebSocket으로 전송
        messagingTemplate.convertAndSend("/topic/admin", dto);
    }

    public NotificationApiDto getNotificationDto() {
        List<Notification> notificationList = notificationRepository.findTop5ByIsReadFalseOrderByCreatedAtDesc();
        List<NotificationResponseDto> notificationResponseDtoList = new ArrayList<>();

        for (Notification notification : notificationList) {


            NotificationResponseDto notificationResponseDto = NotificationResponseDto.builder()
                    .notificationPK(notification.getId())
                    .read(notification.isRead())
                    .content(notification.getContent())
                    .reservationPK(notification.getReservation().getId())
                    .createdAt(Formatter.getLocalDate(notification.getCreatedAt()))
                    .memberName(notification.getMember().getName())
                    .build();

            notificationResponseDtoList.add(notificationResponseDto);
        }
        return NotificationApiDto.builder()
                .totalNonReadCount(notificationRepository.countByIsReadFalse())
                .notificationResponseDtoList(notificationResponseDtoList)
                .build();
    }

    public void markAllAsRead() {
        List<Notification> notifications = notificationRepository.findAllByIsReadFalse(); // 읽지 않은 알림들 가져오기
        for (Notification notification : notifications) {
            notification.setRead(true); // 읽음 처리
        }
        notificationRepository.saveAll(notifications); // 변경된 알림 저장
    }

    public boolean markAsRead(Long id) {
        Optional<Notification> optionalNotification = notificationRepository.findById(id);

        if (optionalNotification.isEmpty()) return false;

        Notification notification = optionalNotification.get();

        notification.setRead(true);

        notificationRepository.save(notification);

        return true;

    }
}
