package Goods.Reservation_Trip.repository;

import Goods.Reservation_Trip.entity.Package;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PackageRepository extends JpaRepository<Package, Long> {
}
