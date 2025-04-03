package Goods.Reservation_Trip.repository;

import Goods.Reservation_Trip.entity.Reservation;
import Goods.Reservation_Trip.enums.ReservationState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation,Long> {
    Page<Reservation> findAll(Pageable pageable);

    Page<Reservation> findByReservationState(ReservationState reservationState,Pageable pageable);

    List<Reservation> findByMemberId(Long memberId);

    List<Reservation> findTop10ByMemberIdOrderByCreatedAtDesc(Long memberId);

    List<Reservation> findByMemberIdAndCreatedAtBetween(Long memberId, LocalDateTime startDate, LocalDateTime endDate);

    List<Reservation> findByMemberIdAndStartDateBetween(Long memberId,LocalDate startDate,LocalDate endDate);

    //공백 구분 없이 예약자 이름 검색
    @Query("SELECT r FROM Reservation r WHERE LOWER(REPLACE(r.member.name, ' ', '')) LIKE LOWER(CONCAT('%', REPLACE(:withoutSpaceSearchText, ' ', ''), '%'))")
    Page<Reservation> findByMemberNameContainingWithoutSpace(@Param("withoutSpaceSearchText") String name, Pageable pageable);

    //공백 구분 없이 주문번호 검색
    @Query("SELECT r FROM Reservation r WHERE LOWER(REPLACE(r.code, ' ', '')) LIKE LOWER(CONCAT('%', REPLACE(:withoutSpaceSearchText, ' ', ''), '%'))")
    Page<Reservation> findByCodeContainingWithoutSpace(@Param("withoutSpaceSearchText") String code, Pageable pageable);

    //공백 구분 없이 패키지명 검색
    @Query("SELECT r FROM Reservation r WHERE LOWER(REPLACE(r.aPackage.packageName, ' ', '')) LIKE LOWER(CONCAT('%', REPLACE(:withoutSpaceSearchText, ' ', ''), '%'))")
    Page<Reservation> findByAPackagePackageNameContainingWithoutSpace(
            @Param("withoutSpaceSearchText") String packageName,
            Pageable pageable
    );

    //공백 구분 없이 주문자 이름 검색 + 주문상태
    @Query("SELECT r FROM Reservation r WHERE LOWER(REPLACE(r.member.name, ' ', '')) LIKE LOWER(CONCAT('%', REPLACE(:withoutSpaceSearchText, ' ', ''), '%')) AND r.reservationState = :reservationState ORDER BY r.reservationState")
    Page<Reservation> findByMemberNameContainingWithoutSpaceAndReservationState(@Param("withoutSpaceSearchText") String name, Pageable pageable, @Param("reservationState") ReservationState reservationState);

    //공백 구분 없이 주문번호 검색 + 주문상태
    @Query("SELECT r FROM Reservation r WHERE LOWER(REPLACE(r.code, ' ', '')) LIKE LOWER(CONCAT('%', REPLACE(:withoutSpaceSearchText, ' ', ''), '%')) AND r.reservationState = :reservationState ORDER BY r.reservationState")
    Page<Reservation> findByCodeContainingWithoutSpaceAndReservationState(@Param("withoutSpaceSearchText") String name, Pageable pageable, @Param("reservationState") ReservationState reservationState);

    //공백 구분 없이 패키지명 검색 + 주문상태
    @Query("SELECT r FROM Reservation r WHERE LOWER(REPLACE(r.aPackage.packageName, ' ', ''))  LIKE LOWER(CONCAT('%', REPLACE(:withoutSpaceSearchText, ' ', ''), '%')) AND r.reservationState = :reservationState ORDER BY r.reservationState")
    Page<Reservation> findByAPackagePackageNameContainingWithoutSpaceAndReservationState(
            @Param("withoutSpaceSearchText") String productName,
            Pageable pageable, @Param("reservationState") ReservationState reservationState
    );
}
