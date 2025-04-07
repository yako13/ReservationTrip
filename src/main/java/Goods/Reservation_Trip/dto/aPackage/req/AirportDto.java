package Goods.Reservation_Trip.dto.aPackage.req;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AirportDto {

    private Long id;

    private String name;

    public AirportDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
