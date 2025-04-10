package Goods.Reservation_Trip.repository.aPackage;

import Goods.Reservation_Trip.entity.PackageOption;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PackageOptionRepository extends JpaRepository<PackageOption, Long> {
//    Optional<PackageOption> findByAPackageId(Long packageId);
}
