package Goods.Reservation_Trip.dto.reservation.res;

import Goods.Reservation_Trip.dto.member.res.MemberResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class ReservationDetailsResponseDto {

    //예약 PK
    private Long reservationPK;

    //예약 번호
    private String code;

    //예약 날짜
    private String createdAt;

    //예약자명
    private String name;

    //예약자 이메일
    private String email;

    //예약자 연락처
    private String phoneNumber;

    //패키지PK
    private Long packagePK;

    //대표이미지
    private String mainImage;

    //패키지명
    private String packageName;

    //여행 시작일
    private String startDate;

    //여행 마감일
    private String endDate;

    //예약 인원 연령대 및 수
    private String reservationMembers;

    //예약 상태
    private String reservationState;

    //총 결제금액
    private String totalPay;

    //제목 태그
    private String tag;

    //옵션
    private List<String> option;

    //카카오페이 결제고유번호
    private String aid;

    //결제 타입
    private String payType;

    //카카오페이 결제승인시각
    private String approvedAt;

    //여행 가는날 출발 시간
    private String s_departureTime;

    //여행 가는날 출발지
    private String s_origin;

    //여행 가는 날 출발 항공편
    private String s_departureFlight;

    //여행 가는 날 도착 시간
    private String s_arrivalTime;

    //여행 가는 날 도착지
    private String s_destination;

    //여행 종료일 출발 시간
    private String e_departureTime;

    //여행 종료일 출발지
    private String e_origin;

    //여행 종료일 출발 항공편
    private String e_departureFlight;

    //여행 종료일 도착 시간
    private String e_arrivalTime;

    //여행 종료일 도착지
    private String e_destination;

    //패키지 연령대별금액
    private List<String> pricesByAgeGroup;

    //결제 금액 상세
    private List<String> detailPay;

    //예약 인원 정보
    private List<MemberResponseDto> memberList;

    //유류할증료
    private String fuelSurcharge;

    //호텔
    private List<String> hotel;

}
