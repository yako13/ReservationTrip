package Goods.Reservation_Trip.dto.HanDto;

import Goods.Reservation_Trip.entity.ReservationDetails;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResPeopleDto {

    //예약 상세 엔티티
    private ReservationDetails reservationDetails;

    //생년월일 변환된것
    private String birthString;

    //휴대폰번호 변환된것
    private String phoneString; // 선택값이므로 null 허용

    //성별 변환한것
    private String gender;


}
