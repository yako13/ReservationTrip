package Goods.Reservation_Trip.repository;

import Goods.Reservation_Trip.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review,Long> {

    List<Review> findByMemberId(Long memberId);
}
