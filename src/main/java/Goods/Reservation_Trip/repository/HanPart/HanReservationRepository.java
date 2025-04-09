package Goods.Reservation_Trip.repository.HanPart;

import Goods.Reservation_Trip.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HanReservationRepository extends JpaRepository<Reservation,Long> {
}
