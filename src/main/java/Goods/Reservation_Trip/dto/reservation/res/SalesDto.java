package Goods.Reservation_Trip.dto.reservation.res;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SalesDto {

    private String date;

    private BigDecimal totalSales;
}
