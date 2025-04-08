package Goods.Reservation_Trip.repository.aPackage;

import Goods.Reservation_Trip.entity.PackageSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface PackageScheduleRepository extends JpaRepository<PackageSchedule, Long> {

    // departureDateOut(출국 시작 일자)가 오늘보다 이전일때 packageStatus(예약 상태) 를 'AVAILABLE' 에서 'CLOSED' 로 수정
    @Modifying
    @Query("UPDATE PackageSchedule p " +
            "SET p.packageStatus = 'CLOSED' " +
            "WHERE p.departureDateOut < CURRENT_DATE " +
            "AND p.packageStatus = 'AVAILABLE'")
    int updateExpiredSchedules();
}
