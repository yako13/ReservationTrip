package Goods.Reservation_Trip.repository.aPackage;

import Goods.Reservation_Trip.entity.PackageCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PackageCategoryRepository extends JpaRepository<PackageCategory, Long> {
    // 대분류 조회
    List<PackageCategory> findByParentIsNull();
}
