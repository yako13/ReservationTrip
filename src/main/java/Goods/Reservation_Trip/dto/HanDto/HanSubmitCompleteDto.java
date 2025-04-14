package Goods.Reservation_Trip.dto.HanDto;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HanSubmitCompleteDto {

    //예약번호
    private String code;

    //출발일
    private String startDate;

    //상품명
    private String packageName;

    //결제 금액
    private String totalPay;

    //로그인 여부
    private boolean loginNo;

    //예약이 만석인지 여부
    private boolean resvFull;


}
