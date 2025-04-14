package Goods.Reservation_Trip.repository.HanPart;


import Goods.Reservation_Trip.entity.PackageCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HanPackageCategoryRepository  extends JpaRepository<PackageCategory,Long> {

    List<PackageCategory> findByDepth(int depth);

}
