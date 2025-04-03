package Goods.Reservation_Trip.dto.aPackage.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

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

    private LocalDate departureDateOut;

    private LocalDate arrivalDateReturn;

    private int period;
}
