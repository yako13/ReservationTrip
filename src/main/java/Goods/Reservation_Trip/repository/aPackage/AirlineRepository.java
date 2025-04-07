package Goods.Reservation_Trip.repository.aPackage;

import Goods.Reservation_Trip.entity.Airline;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AirlineRepository extends JpaRepository<Airline, Long> {

    boolean existsByCode(String code);

    Optional<Airline> findByCode(String code);
}
