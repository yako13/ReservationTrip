package Goods.Reservation_Trip.repository.aPackage;

import Goods.Reservation_Trip.entity.PackageScheduleDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PackageScheduleDetailsRepository extends JpaRepository<PackageScheduleDetails, Long> {

    // packageStatus(예약 상태)가 AVAILABLE 이면서 departureDateOut(출국 시작 일자)가 가장 빠른 상품을 PK 별로 하나씩 보여줌
    @Query("SELECT p FROM PackageScheduleDetails p " +
            "WHERE p.packageSchedule.packageStatus = 'AVAILABLE' " +
            "AND p.packageSchedule.departureDateOut = ( " +
            "   SELECT MIN(ps.packageSchedule.departureDateOut) " +
            "   FROM PackageScheduleDetails ps " +
            "   WHERE ps.packageSchedule.aPackage = p.packageSchedule.aPackage " +
            "   AND ps.packageSchedule.packageStatus = 'AVAILABLE')")
    Page<PackageScheduleDetails> findEarliestDepartureDateOutAvailableSchedulesPerPackage(Pageable pageable);
}
