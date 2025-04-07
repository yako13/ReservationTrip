package Goods.Reservation_Trip.repository.aPackage;

import Goods.Reservation_Trip.entity.AirPort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AirportRepository extends JpaRepository<AirPort, Long> {

    // 공항 코드가 있는지 확인
    boolean existsByCode(String code);

    List<AirPort> findByCategoryId(Long categoryId);

    Optional<AirPort> findByName(String name);

}
