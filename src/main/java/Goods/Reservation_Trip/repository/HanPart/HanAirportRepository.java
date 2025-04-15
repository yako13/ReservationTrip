package Goods.Reservation_Trip.repository.HanPart;

import Goods.Reservation_Trip.entity.Airport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HanAirportRepository extends JpaRepository<Airport,Long> {




}
