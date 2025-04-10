package Goods.Reservation_Trip.repository.HanPart;

import Goods.Reservation_Trip.entity.PackageSchedule;
import Goods.Reservation_Trip.enums.PackageStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface HanPackageScheduleRepository extends JpaRepository<PackageSchedule,Long> {

    // 특정 패키지에 대해 PackageStatus 필터로 리스트로 가져옴
    @Query("SELECT ps FROM PackageSchedule ps WHERE ps.aPackage.id = :packageId AND ps.packageStatus = :status")
    List<PackageSchedule> findAvailableSchedules(@Param("packageId") Long packageId, @Param("status") PackageStatus status);


    //패키지안의 여행일정중 PackageStatus 필터로 날짜만 가져옴
    @Query("SELECT ps.departureDateOut FROM PackageSchedule ps WHERE ps.aPackage.id = :packageId AND ps.packageStatus = :status")
    List<LocalDate> findDepartureDatesByPackageIdAndStatus(@Param("packageId") Long packageId, @Param("status") PackageStatus status);


}
