package Goods.Reservation_Trip.dto.member.res;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberResponseDto {

    //PK
    private Long id;

    //이메일
    private String email;

    //이름
    private String name;

    //OAuth2 제공자
    private String provider;

    //생년월일
    private String birth;

    //성별
    private boolean gender;

    //연락처
    private String phoneNumber;

    //여권번호
    private String passportNumber;

    //예약자여부
    private boolean defaultReservation;
}
