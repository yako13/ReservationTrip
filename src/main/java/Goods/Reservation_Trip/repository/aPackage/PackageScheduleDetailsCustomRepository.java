package Goods.Reservation_Trip.repository.aPackage;

import Goods.Reservation_Trip.entity.PackageScheduleDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface PackageScheduleDetailsCustomRepository {

    // 패키지 이름 기준 검색을 할때 (예약 상태) 'packageStatus = AVAILABLE' 이면서 (귀국출발일자) 'departureDateOut' 가 가장 빠른 일정 pk당 1건씩 조회
    Page<PackageScheduleDetails> findAvailableEarliestByPackageNameContaining(String name, Pageable pageable, String sort);

    // 패키지의 여행지 카태고리로 결과 조회
    Page<PackageScheduleDetails> findByCategoryAndSubCategories(Long mainCategoryId, Long subCategoryId, Long smallCategoryId, String sort, Pageable pageable);
}
