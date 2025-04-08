package Goods.Reservation_Trip.dto.HanDto;

import Goods.Reservation_Trip.entity.Reservation;
import lombok.*;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResDetailPageDto {

    //예약 엔티티
    private Reservation reservation;

    //-예약상세 엔티티와 핸드폰 , 생년월일 변환된 값이 포함된 리스트-

    //성인
    private List<ResPeopleDto> resAdultList;
    //아동
    private List<ResPeopleDto> resChildList;
    //유아
    private List<ResPeopleDto> resBabyList;

    //---formatter로 변환된 값---

    //여행 출발일 변환된것
    private String tripStartString;

    //여행 도착일 변환된것
    private String tripEndString;

    //예약일 변환된것
    private String ResDate;

    //성인 가격 총합
    //Package의 유류할증료 포함 가격이 성인 가격
    private String adultSumPriceString;

    //아동 가격 총합
    //Package의 유류할증료 포함 가격이 성인 가격 그리고 아동도 같은 가격
    private String childSumPriceString;

    //유아 가격 총합
    //Package의 유아 인당가격만 (유류할증료가 붙지 않는다)
    private String babySumPriceString;

    //총 가격
    private String totalPriceString;




}
