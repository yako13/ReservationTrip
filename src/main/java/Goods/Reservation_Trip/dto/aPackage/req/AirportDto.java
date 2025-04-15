package Goods.Reservation_Trip.dto.aPackage.req;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AirportDto {

    private Long id;

    private String name;

    private String code;
}
