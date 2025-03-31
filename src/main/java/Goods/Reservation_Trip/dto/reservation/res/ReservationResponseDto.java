package Goods.Reservation_Trip.dto.reservation.res;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationResponseDto {

    //예약 PK
    private Long reservationPK;

    //패키지명
    private String packageName;

    //예약자명
    private String memberName;

    //예약번호
    private String reservationCode;

    //예약상태
    private String reservationState;

    //총 결제금액
    private String totalPay;

    //예약날짜
    private String createdAt;

    //수정날짜
    private String modifiedAt;
}
