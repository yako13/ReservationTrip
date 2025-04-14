package Goods.Reservation_Trip.repository.aPackage;

import Goods.Reservation_Trip.entity.PackageCategory;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PackageCategoryRepository extends JpaRepository<PackageCategory, Long> {
    // 대분류 조회
    List<PackageCategory> findByParentIsNull();

    List<PackageCategory> findByDepth(int depth);

    // 카테고리 이름 찾기
    Optional<PackageCategory> findByName(String name);

    // 카테고리 이름이 있는지
    boolean existsByName(String name);

    List<PackageCategory> findByParentIdAndDepth(Long parentId,int depth);

    Optional<PackageCategory> findByNameAndDepth(String name,int depth);

    @Query("SELECT COUNT(p) FROM Package p WHERE p.mainCategory.id = :id OR p.subCategory.id = :id OR p.smallCategory.id = :id")
    int countByAnyCategory(@Param("id") Long id);

}
