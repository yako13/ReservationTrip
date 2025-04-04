package Goods.Reservation_Trip.repository.aPackage;

import Goods.Reservation_Trip.entity.PackageSchedule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface PackageScheduleRepository extends JpaRepository<PackageSchedule, Long> {

    // packageStatus(예약 상태)가 AVAILABLE 이면서 departureDateOut(출국 시작 일자)가 가장 빠른 상품을 PK 별로 하나씩 보여줌
    @Query("SELECT p FROM PackageSchedule p " +
            "WHERE p.packageStatus = 'AVAILABLE' " +
            "AND p.departureDateOut = ( " +
            "    SELECT MIN(ps.departureDateOut) " +
            "    FROM PackageSchedule ps " +
            "    WHERE ps.aPackage = p.aPackage" +
            " AND ps.packageStatus = 'AVAILABLE')")
    Page<PackageSchedule> findEarliestDepartureDateOutAvailableSchedulesPerPackage(Pageable pageable);

    // departureDateOut(출국 시작 일자)가 오늘보다 이전일때 packageStatus(예약 상태) 를 'CLOSED' 로 수정
    @Modifying
    @Query("UPDATE PackageSchedule p " +
            "SET p.packageStatus = 'CLOSED' " +
            "WHERE p.departureDateOut < CURRENT_DATE " +
            "AND p.packageStatus = 'AVAILABLE'")
    int updateExpiredSchedules();
}
