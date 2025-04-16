package Goods.Reservation_Trip.repository.HanPart;


import Goods.Reservation_Trip.entity.PackageCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HanPackageCategoryRepository extends JpaRepository<PackageCategory, Long> {

    List<PackageCategory> findByDepth(int depth);

    //한국이 아닌 카테고리 전부 가져옴
    List<PackageCategory> findByDepthAndNameNot(int depth, String name);


    //한국이 아니고 Depth3인거 전부 가져옴
    @Query("""
            SELECT c FROM PackageCategory c
            WHERE c.depth = 3
            AND c.parent.parent.depth = 1
            AND c.parent.parent.name <> '한국'
            """)
    List<PackageCategory> findDepth3WhoseTopParentIsNotKorea();


}
