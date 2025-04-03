package Goods.Reservation_Trip.repository;

import Goods.Reservation_Trip.entity.ReservationDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationDetailsRepository extends JpaRepository<ReservationDetails,Long> {
}
