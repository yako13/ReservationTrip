package Goods.Reservation_Trip.repository.aPackage;

import Goods.Reservation_Trip.entity.Airport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AirportRepository extends JpaRepository<Airport, Long> {

    // 공항 코드가 있는지 확인
    boolean existsByCode(String code);

    List<Airport> findByCategoryId(Long categoryId);

    Optional<Airport> findByCode(String code);
}
