package Goods.Reservation_Trip.dto.HanDto;


import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CityDto {
    //카테고리 pk
    private Long id;
    //카테고리의 도시명
    private String name;

}
