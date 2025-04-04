package Goods.Reservation_Trip.dto.HanDto;

import Goods.Reservation_Trip.dto.member.res.MemberResponseDto;
import Goods.Reservation_Trip.entity.Package;
import Goods.Reservation_Trip.entity.PackageSchedule;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResvPageDto { //예약 페이지를 보여주기위한 dto

    //성인 유아 아동 인원수 및 출발일 패키지 회원pk
    private PackResvDto packResvDto;

    //여행 출발일 변환된것
    private String tripStartString;
    //여행 도착일 변환된것
    private String tripEndString;

    //여행 도착일
    private LocalDate tripEnd;

    //회원 정보 dto
    private MemberResponseDto memberResponseDto;

    //회원 성별
    private String gender;

    //회원 전화번호 변환
    private String phoneNum;

    //패키지 엔티티
    private Package packageEntity;

    //선택된 여행일정 엔티티
    private PackageSchedule packageSchedule;

    //성인 가격 총합
    //Package의 유류할증료 포함 가격이 성인 가격
    private BigDecimal adultSumPrice;

    //아동 가격 총합
    //Package의 유류할증료 포함 가격이 성인 가격 그리고 아동도 같은 가격
    private BigDecimal childSumPrice;

    //유아 가격 총합
    //Package의 유아 인당가격만 (유류할증료가 붙지 않는다)
    private BigDecimal babySumPrice;

    //총 가격
    private BigDecimal totalPrice;

    //---formatter로 변환된 값---

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
