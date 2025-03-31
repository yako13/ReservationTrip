package Goods.Reservation_Trip.repository;

import Goods.Reservation_Trip.entity.PackageImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PackageImageRepository extends JpaRepository<PackageImage, Long> {
    List<PackageImage> findByaPackageId(Long aPackageId);
}
