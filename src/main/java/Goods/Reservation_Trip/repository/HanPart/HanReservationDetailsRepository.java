package Goods.Reservation_Trip.repository.HanPart;

import Goods.Reservation_Trip.entity.ReservationDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HanReservationDetailsRepository extends JpaRepository<ReservationDetails,Long> {
}
