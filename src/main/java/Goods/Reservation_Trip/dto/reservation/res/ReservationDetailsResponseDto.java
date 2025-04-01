package Goods.Reservation_Trip.dto.reservation.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
}
