package Goods.Reservation_Trip.repository.aPackage;

import Goods.Reservation_Trip.entity.PackageSchedule;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface PackageScheduleRepository extends JpaRepository<PackageSchedule, Long> {

    // departureDateOut(출국 시작 일자)가 오늘보다 이전일때 packageStatus(예약 상태) 를 'AVAILABLE' 에서 'CLOSED' 로 수정
    @Modifying
    @Query("UPDATE PackageSchedule p " +
            "SET p.packageStatus = 'CLOSED' " +
            "WHERE p.departureDateOut <= :now " +
            "AND p.packageStatus = 'AVAILABLE'")
    int updateExpiredSchedules(@Param("now")LocalDate now);

    @Modifying
    @Query("UPDATE PackageSchedule p " +
            "SET p.packageStatus = 'CLOSED' " +
            "WHERE p.departureDateOut <= :cutoff " +
            "AND p.packageStatus = 'AVAILABLE'")
    int updateExpiredSchedulesNextDay(@Param("cutoff") LocalDate cutoff);

    @EntityGraph(attributePaths = "packageScheduleDetails")
    List<PackageSchedule> findByaPackage_Id(Long id);
}
