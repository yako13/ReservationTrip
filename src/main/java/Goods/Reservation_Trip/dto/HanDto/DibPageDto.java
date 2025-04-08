package Goods.Reservation_Trip.dto.HanDto;

import Goods.Reservation_Trip.entity.Dib;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DibPageDto {
    //찜 엔티티
    private Dib dib;

    //성인 1인당 가격 (fuelSurchargeIncluded)
    private String price;




}
