package Goods.Reservation_Trip.repository.aPackage;

import Goods.Reservation_Trip.entity.Airport;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AirportRepository extends JpaRepository<Airport, Long> {

    @Query("""
    SELECT DISTINCT a FROM Airport a
    JOIN a.categoryList c
    WHERE c.name IN :categoryNames
""")
    List<Airport> findAirportsByCategoryNames(@Param("categoryNames") List<String> categoryNames);

    // 공항 코드가 있는지 확인
    boolean existsByCode(String code);

    Optional<Airport> findByName(String name);

    @Query("SELECT a FROM Airport a JOIN a.categoryList c WHERE c.id = :categoryId")
    List<Airport> findByCategoryId(@Param("categoryId") Long categoryId);

    @Query("""
    SELECT COUNT(a) > 0 
    FROM Airport a 
    JOIN a.categoryList c 
    WHERE 
        (c.id = :categoryId1 AND a.name = :name)
        OR 
        (c.id = :categoryId2 AND a.code = :code)
    """)
    boolean existsByCategoryIdAndNameOrCategoryIdAndCode(
            @Param("categoryId1") Long categoryId1,
            @Param("name") String name,
            @Param("categoryId2") Long categoryId2,
            @Param("code") String code
    );

    @Query("""
    SELECT a 
    FROM Airport a 
    JOIN a.categoryList c 
    WHERE c.id = :categoryId AND a.name = :name
""")
    Optional<Airport> findByCategoryIdAndName(
            @Param("categoryId") Long categoryId,
            @Param("name") String name
    );

    List<Airport> findByCategoryListIsEmpty();

    Optional<Airport> findByNameAndCode(String name,String code);

    Optional<Airport> findByCode(String code);

    //이름 또는 코드
    Optional<Airport> findByNameOrCode( String name ,String code);

    //공항이랑 연결된 스케쥴이 있는지 확인
    @Query(value = "SELECT EXISTS ( SELECT 1 FROM package_schedule_details WHERE departure_point_out = :airportId OR arrival_point_out = :airportId OR departure_point_return = :airportId OR arrival_point_return = :airportId) ", nativeQuery = true)
    Integer isAirportUsedRaw(@Param("airportId") Long airportId);
}
