package Goods.Reservation_Trip.repository;

import Goods.Reservation_Trip.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification,Long> {

    //최신순으로 5개만
    List<Notification> findTop5ByIsReadFalseOrderByCreatedAtDesc();

    //안읽은 메세지 개수
    long countByIsReadFalse();

    List<Notification> findAllByIsReadFalse();
}
