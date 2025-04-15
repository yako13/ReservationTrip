package Goods.Reservation_Trip.dto.aPackage.res;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PackageOptionEditResponseDto {

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
