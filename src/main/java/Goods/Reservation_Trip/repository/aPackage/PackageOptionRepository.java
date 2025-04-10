package Goods.Reservation_Trip.repository.aPackage;

import Goods.Reservation_Trip.entity.Package;
import Goods.Reservation_Trip.entity.PackageOption;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PackageOptionRepository extends JpaRepository<PackageOption, Long> {
    @Query("SELECT po FROM PackageOption po WHERE po.aPackage.id = :id")
    Optional<PackageOption> findByPackageId(@Param("id") Long id);
}
