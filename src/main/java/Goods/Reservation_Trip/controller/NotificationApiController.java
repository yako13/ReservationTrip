package Goods.Reservation_Trip.controller;

import Goods.Reservation_Trip.dto.review.res.NotificationApiDto;
import Goods.Reservation_Trip.service.reservation.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notification")
public class NotificationApiController {
    private final NotificationService notificationService;

    @GetMapping
    public NotificationApiDto getNotification(){
        return notificationService.getNotificationDto();
    }

    @PostMapping("/markAllAsRead")
    public ResponseEntity<?> markAllNotificationsAsRead() {
        try {
            notificationService.markAllAsRead();
            return ResponseEntity.ok().body("모든 알림을 읽음 처리했습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("알림 읽음 처리 실패: " + e.getMessage());
        }
    }

    @PostMapping("/mark-read/{id}")
    public ResponseEntity<?> markAsRead(@PathVariable Long id) {
        boolean updated = notificationService.markAsRead(id);

        if (updated) {
            return ResponseEntity.ok().body("읽음 처리 완료");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("알림을 찾을 수 없습니다");
        }
    }

}
