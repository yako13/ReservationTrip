package Goods.Reservation_Trip.dto.aPackage.req;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PackageAirportDto {

    private String airportName;

    private String airportCode;

    private String small;
}
