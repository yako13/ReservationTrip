package Goods.Reservation_Trip.dto.aPackage.req;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PackageOptionRequestDto {

    private Long id;
    /**
     * 가이드 유무
     */
    private boolean guide;

    /**
     * 항공료 포함 유무
     */
    private boolean airFare;

    /**
     * 호텔비 포함 유무
     */
    private boolean hotelFee;

    /**
     * 쇼핑 유무
     */
    private boolean shopping;
}
