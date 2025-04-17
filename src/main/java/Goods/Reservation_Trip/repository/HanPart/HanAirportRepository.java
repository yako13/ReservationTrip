package Goods.Reservation_Trip.repository.HanPart;

import Goods.Reservation_Trip.entity.Airport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HanAirportRepository extends JpaRepository<Airport, Long> {

    @Query("""
            SELECT DISTINCT a FROM Airport a
            JOIN a.categoryList c3
            JOIN c3.parent c2
            JOIN c2.parent c1
            WHERE c1.depth = 1 AND c1.name = '한국'
            """)
    List<Airport> findAllAirportsWithTopCategoryNamedKorea();


}
