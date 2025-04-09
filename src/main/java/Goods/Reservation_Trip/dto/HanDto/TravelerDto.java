package Goods.Reservation_Trip.dto.HanDto;

import Goods.Reservation_Trip.enums.AgeGroup;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TravelerDto {

    //이름
    private String name;
    //성별
    private Boolean gender;  // true = 남, false = 여
    //생년월일
    private String birth;
    //휴대폰번호
    private String phone; // 선택값이므로 null 허용
    //여권번호
    private String passportNum;
    //예약자 여부
    private boolean resCheck;

    //생년월일 변환된것
    private String birthString;

    //휴대폰번호 변환된것
    private String phoneString; // 선택값이므로 null 허용

    private AgeGroup ageGroup;


}
