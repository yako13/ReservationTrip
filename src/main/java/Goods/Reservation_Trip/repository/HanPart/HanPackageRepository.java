package Goods.Reservation_Trip.repository.HanPart;

import Goods.Reservation_Trip.entity.Package;
import Goods.Reservation_Trip.enums.PackageStatus;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface HanPackageRepository extends JpaRepository<Package, Long> {

    Page<Package> findByPackageStatus(PackageStatus status, Pageable pageable);

    @Query("SELECT p FROM Package p " +
            "JOIN p.packageOption o " +
            "WHERE (:airfare IS NULL OR o.airfare = :airfare) " +
            "AND (:hotelFee IS NULL OR o.hotelFee = :hotelFee) " +
            "AND (:guide IS NULL OR o.guide = :guide) " +
            "AND (:noShopping IS NULL OR o.noShopping = :noShopping)")
    List<Package> findByOptions(@Param("airfare") Boolean airfare,
                                @Param("hotelFee") Boolean hotelFee,
                                @Param("guide") Boolean guide,
                                @Param("noShopping") Boolean noShopping);


    @Query("SELECT DISTINCT p FROM Package p " +
            "JOIN p.packageOption o " +
            "JOIN p.packageScheduleList s " +
            "JOIN s.packageScheduleDetails sd " +
            "JOIN sd.airlineOut ao " +
            "WHERE p.packageStatus = 'AVAILABLE' " +
            "AND (:airfare IS NULL OR o.airfare = :airfare) " +
            "AND (:hotelFee IS NULL OR o.hotelFee = :hotelFee) " +
            "AND (:guide IS NULL OR o.guide = :guide) " +
            "AND (:noShopping IS NULL OR o.noShopping = :noShopping) " +
            "AND (:startDate IS NULL OR s.departureDateOut >= :startDate) " +
            "AND (:endDate IS NULL OR s.arrivalDateReturn <= :endDate) " +
            "AND (:categoryId IS NULL OR " +
            "     (:categoryDepth = 1 AND p.mainCategory.id = :categoryId) OR " +
            "     (:categoryDepth = 2 AND p.subCategory.id = :categoryId) OR " +
            "     (:categoryDepth = 3 AND p.smallCategory.id = :categoryId)) " +
            "AND (:period IS NULL OR p.period = :period) " +
            "AND (:airportId IS NULL OR ao.id = :airportId) " +
            "AND (:keyword IS NULL OR :keyword = '' OR " +
            "LOWER(p.packageName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(p.description) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<Package> filterPackagesWithConditions(
            @Param("airfare") Boolean airfare,
            @Param("hotelFee") Boolean hotelFee,
            @Param("guide") Boolean guide,
            @Param("noShopping") Boolean noShopping,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("categoryId") Long categoryId,
            @Param("categoryDepth") Integer categoryDepth,
            @Param("period") Integer period,
            @Param("keyword") String keyword,
            @Param("airportId") Long airportId,
            Pageable pageable);





    @Query("SELECT p FROM Package p " +
            "JOIN p.packageOption o " +
            "JOIN p.packageScheduleList s " +
            "JOIN s.packageScheduleDetails sd " +
            "JOIN sd.airlineOut ao " +
            "WHERE p.packageStatus = 'AVAILABLE' " +
            "AND (:airfare IS NULL OR o.airfare = :airfare) " +
            "AND (:hotelFee IS NULL OR o.hotelFee = :hotelFee) " +
            "AND (:guide IS NULL OR o.guide = :guide) " +
            "AND (:noShopping IS NULL OR o.noShopping = :noShopping) " +
            "AND (:startDate IS NULL OR s.departureDateOut >= :startDate) " +
            "AND (:endDate IS NULL OR s.arrivalDateReturn <= :endDate) " +
            "AND (:categoryId IS NULL OR " +
            "     (:categoryDepth = 1 AND p.mainCategory.id = :categoryId) OR " +
            "     (:categoryDepth = 2 AND p.subCategory.id = :categoryId) OR " +
            "     (:categoryDepth = 3 AND p.smallCategory.id = :categoryId)) " +
            "AND (:period IS NULL OR p.period = :period) " +
            "AND (:airportId IS NULL OR ao.id = :airportId) " +
            "AND (:keyword IS NULL OR :keyword = '' OR " +
            "LOWER(p.packageName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(p.description) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
            "GROUP BY p " +
            "ORDER BY SUM(s.reservedMemberCount) DESC")
    Page<Package> filterByReservationCount(
            @Param("airfare") Boolean airfare,
            @Param("hotelFee") Boolean hotelFee,
            @Param("guide") Boolean guide,
            @Param("noShopping") Boolean noShopping,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("categoryId") Long categoryId,
            @Param("categoryDepth") Integer categoryDepth,
            @Param("period") Integer period,
            @Param("keyword") String keyword,
            @Param("airportId") Long airportId,
            Pageable pageable);





}
