package Goods.Reservation_Trip.repository.aPackage;

import Goods.Reservation_Trip.entity.*;
import Goods.Reservation_Trip.enums.PackageStatus;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class PackageScheduleDetailsCustomRepositoryImpl implements PackageScheduleDetailsCustomRepository {

    private final JPAQueryFactory queryFactory;
    QPackageScheduleDetails details = QPackageScheduleDetails.packageScheduleDetails;
    QPackageSchedule schedule = QPackageSchedule.packageSchedule;
    QPackage aPackage = QPackage.package$;


    @Override
    public Page<PackageScheduleDetails> findAvailableEarliestByPackageNameContaining(String name, Pageable pageable) {

        // 서브쿼리: 해당 이름을 포함하는 패키지 중 '가장 빠른 출발일' 을 가진 스케줄 날짜 조회
        JPQLQuery<LocalDate> minDateSubQuery = JPAExpressions
                .select(schedule.departureDateOut.min())
                .from(details)
                .join(details.packageSchedule, schedule)
                .where(
                        aPackage.packageName.containsIgnoreCase(name),
                        schedule.packageStatus.eq(PackageStatus.AVAILABLE)
                );
        // 메인 쿼리: 가장 빠른 출발일을 가진 스케줄의 상세 정보 packageScheduleDetails 목록 조회
        List<PackageScheduleDetails> results = queryFactory
                .selectFrom(details)
                .from(details)
                .join(details.packageSchedule, schedule)
                .join(schedule.aPackage, aPackage)
                .where(
                        aPackage.packageName.containsIgnoreCase(name),
                        schedule.packageStatus.eq(PackageStatus.AVAILABLE),
                        schedule.departureDateOut.eq(minDateSubQuery)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(schedule.departureDateOut.asc())
                .fetch();
        // 전체 결과 개수 조회 (페이지네이션 처리위함)
        Long total = queryFactory
                .select(schedule.count())
                .from(schedule)
                .join(schedule.aPackage, aPackage)
                .where(
                        aPackage.packageName.containsIgnoreCase(name),
                        schedule.packageStatus.eq(PackageStatus.AVAILABLE),
                        schedule.departureDateOut.eq(minDateSubQuery)
                )
                .fetchOne();
        // page 객체로 변환
        return new PageImpl<>(results, pageable, total == null ? 0 : total);
    }
}
