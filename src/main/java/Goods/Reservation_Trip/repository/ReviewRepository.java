package Goods.Reservation_Trip.repository;

import Goods.Reservation_Trip.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review,Long> {
}
