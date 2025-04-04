package Goods.Reservation_Trip.repository;

import Goods.Reservation_Trip.entity.ReviewImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewImageRepository extends JpaRepository<ReviewImage,Long> {
    void deleteByReviewId(Long reviewId);
}
