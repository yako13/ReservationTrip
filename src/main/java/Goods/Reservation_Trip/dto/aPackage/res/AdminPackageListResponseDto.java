package Goods.Reservation_Trip.dto.aPackage.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminPackageListResponseDto {

    private Long id;

    private String name;

    private String mainImagePath;

    private BigDecimal fuelSurchargeIncluded;

    private String departurePointOut;

    private String arrivalPointOut;

    private int maximumMember;

    private String period;
}
