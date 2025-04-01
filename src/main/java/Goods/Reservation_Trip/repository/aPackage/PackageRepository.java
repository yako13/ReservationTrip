package Goods.Reservation_Trip.repository.aPackage;

import Goods.Reservation_Trip.entity.Package;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PackageRepository extends JpaRepository<Package, Long> {
    @Query("SELECT p FROM Package p LEFT JOIN FETCH p.packageSchedule")
    Page<Package> findAllWithSchedule(Pageable pageable);
}
