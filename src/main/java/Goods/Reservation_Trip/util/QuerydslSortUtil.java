package Goods.Reservation_Trip.util;

import Goods.Reservation_Trip.entity.QPackage;
import Goods.Reservation_Trip.entity.QPackageSchedule;
import com.querydsl.core.types.OrderSpecifier;

public class QuerydslSortUtil {

    // 정렬 순서 설정
    public static OrderSpecifier<?> getOrderSpecifier(String sort, QPackage qPackage, QPackageSchedule qPackageSchedule) {

        switch (sort) {
            case "price_asc" -> {
                return qPackage.fuelSurchargeIncluded.asc();
            }
            case "price_desc" -> {
                return qPackage.fuelSurchargeIncluded.desc();
            }
            case "createdAt_asc" -> {
                return qPackage.createdAt.asc();
            }
            case "departureDateOut_asc" -> {
                return qPackageSchedule.departureDateOut.asc();
            }
            default -> {
                return qPackage.createdAt.desc();

            }
        }
    }
}
