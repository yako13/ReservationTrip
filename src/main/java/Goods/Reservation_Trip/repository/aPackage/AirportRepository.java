package Goods.Reservation_Trip.repository.aPackage;

import Goods.Reservation_Trip.entity.Airport;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AirportRepository extends JpaRepository<Airport, Long> {

    // 공항 코드가 있는지 확인
    boolean existsByCode(String code);

    Optional<Airport> findByName(String name);

    List<Airport> findByCategoryId(Long categoryId);

    Optional<Airport> findByNameAndCode(String name,String code);

    Optional<Airport> findByCode(String code);

    //이름 또는 코드
    Optional<Airport> findByNameOrCode( String name ,String code);

    //공항이랑 연결된 스케쥴이 있는지 확인
    @Query(value = "SELECT EXISTS ( SELECT 1 FROM package_schedule_details WHERE departure_point_out = :airportId OR arrival_point_out = :airportId OR departure_point_return = :airportId OR arrival_point_return = :airportId) ", nativeQuery = true)
    Integer isAirportUsedRaw(@Param("airportId") Long airportId);
}
