package Goods.Reservation_Trip.repository.aPackage.impl;

import Goods.Reservation_Trip.entity.PackageScheduleDetails;
import Goods.Reservation_Trip.entity.QPackage;
import Goods.Reservation_Trip.entity.QPackageSchedule;
import Goods.Reservation_Trip.entity.QPackageScheduleDetails;
import Goods.Reservation_Trip.enums.PackageStatus;
import Goods.Reservation_Trip.repository.aPackage.PackageScheduleDetailsCustomRepository;
import Goods.Reservation_Trip.util.QuerydslSortUtil;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class PackageScheduleDetailsCustomRepositoryImpl implements PackageScheduleDetailsCustomRepository {

    private final JPAQueryFactory queryFactory;
    QPackageScheduleDetails details = QPackageScheduleDetails.packageScheduleDetails;
    QPackageSchedule schedule = QPackageSchedule.packageSchedule;
    QPackage aPackage = new QPackage("aPackage");


    @Override
    public Page<PackageScheduleDetails> findAvailableEarliestByPackageNameContaining(String name, Pageable pageable, String sort) {

        BooleanBuilder whereBuilder = new BooleanBuilder();
        whereBuilder.and(schedule.packageStatus.eq(PackageStatus.AVAILABLE));
        if (name != null && !name.isEmpty()) {
            whereBuilder.and(aPackage.packageName.containsIgnoreCase(name));
        }

        // sort 적용하기
        OrderSpecifier<?> orderSpecifier = QuerydslSortUtil.getOrderSpecifier(sort, aPackage, schedule);

        // 서브쿼리: 해당 이름을 포함하는 패키지 중 '가장 빠른 출발일' 을 가진 스케줄 날짜 조회
        JPQLQuery<Long> detailIdsWithMinDatePerPackage = JPAExpressions
                .select(details.id.min())
                .from(details)
                .join(details.packageSchedule, schedule)
                .join(schedule.aPackage, aPackage)
                .where(whereBuilder)
                .groupBy(aPackage.id);

        // 메인 쿼리: 가장 빠른 출발일을 가진 스케줄의 상세 정보 packageScheduleDetails 목록 조회
        List<PackageScheduleDetails> results = queryFactory
                .selectFrom(details)
                .join(details.packageSchedule, schedule).fetchJoin()
                .join(schedule.aPackage, aPackage).fetchJoin()
                .where(details.id.in(detailIdsWithMinDatePerPackage))
                .orderBy(orderSpecifier)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 전체 결과 개수 조회 (페이지네이션 처리위함)
        Long total = queryFactory
                .select(schedule.count())
                .from(schedule)
                .join(schedule.aPackage, aPackage)
                .where(details.id.in(detailIdsWithMinDatePerPackage))
                .fetchOne();
        // page 객체로 변환
        return new PageImpl<>(results, pageable, total == null ? 0 : total);
    }


    @Override
    public Page<PackageScheduleDetails> findByCategoryAndSubCategories(Long mainCategoryId,
                                                                       Long subCategoryId,
                                                                       Long smallCategoryId,
                                                                       String sort,
                                                                       Pageable pageable) {
        BooleanBuilder whereBuilder = new BooleanBuilder();
        whereBuilder.and(schedule.packageStatus.eq(PackageStatus.AVAILABLE));

        if (mainCategoryId != null) {
            whereBuilder.and(aPackage.mainCategory.id.eq(mainCategoryId));
        }
        if (subCategoryId != null) {
            whereBuilder.and(aPackage.subCategory.id.eq(subCategoryId));
        }
        if (smallCategoryId != null) {
            whereBuilder.and(aPackage.smallCategory.id.eq(smallCategoryId));
        }

        // sort 적용하기
        OrderSpecifier<?> orderSpecifier = QuerydslSortUtil.getOrderSpecifier(sort, aPackage, schedule);

        JPQLQuery<Long> detailIdsWithMinDatePerPackage = JPAExpressions
                .select(details.id.min())
                .from(details)
                .join(details.packageSchedule, schedule)
                .join(schedule.aPackage, aPackage)
                .where(whereBuilder)
                .groupBy(aPackage.id);

        List<PackageScheduleDetails> results = queryFactory
                .selectFrom(details)
                .from(details)
                .join(details.packageSchedule, schedule).fetchJoin()
                .join(schedule.aPackage, aPackage).fetchJoin()
                .where(details.id.in(detailIdsWithMinDatePerPackage))
                .orderBy(orderSpecifier)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(schedule.count())
                .from(details)
                .where(details.id.in(detailIdsWithMinDatePerPackage))
                .fetchOne();

        return new PageImpl<>(results, pageable, total == null ? 0 : total);
    }
}
