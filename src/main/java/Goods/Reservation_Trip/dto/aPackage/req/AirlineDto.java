package Goods.Reservation_Trip.dto.aPackage.req;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AirlineDto {

    private Long id;

    private String code;

    private String name;
}
