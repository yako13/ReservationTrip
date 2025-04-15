package Goods.Reservation_Trip.dto.HanDto;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PackApiDto {

    //여행 출국 비행기
    private String startAirplane;

    //여행 귀국 비행기
    private String endAirplane;

    //여행 기간 변환
    private String tripDate;

    //여행 출발 비행기 이륙 날짜 및 시간
    private String tripStartUp;

    //여행 출발 비행기 착륙 날짜 및 시간
    private String tripStartDown;

    //여행 도착 비행기 이륙 날짜 및 시간
    private String tripEndUp;

    //여행 도착 비행기 착륙 날짜 및 시간
    private String tripEndDown;

    //예약 가능 여부
    private boolean resvYes;

    //이미 예약한 인원
    private int resvPeople;

    //예약 가능 인원
    private int resvOkPeople;

    //최소 예약 인원
    private int resvMinPeople;

}
