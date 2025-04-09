package Goods.Reservation_Trip.repository.aPackage;

import Goods.Reservation_Trip.entity.PackageCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PackageCategoryRepository extends JpaRepository<PackageCategory, Long> {
    // 대분류 조회
    List<PackageCategory> findByParentIsNull();

    // 카테고리 이름 찾기
    Optional<PackageCategory> findByName(String name);

    // 카테고리 이름이 있는지
    boolean existsByName(String name);
}
