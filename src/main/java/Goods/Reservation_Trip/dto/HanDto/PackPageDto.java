package Goods.Reservation_Trip.dto.HanDto;

import Goods.Reservation_Trip.entity.Package;
import Goods.Reservation_Trip.entity.PackageImage;
import Goods.Reservation_Trip.entity.PackageScheduleDetails;
import lombok.*;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PackPageDto {

    //패키지 엔티티
    private Package packageEntity;
    //여행 일정 디테일 (인덱스 0번 꺼의 디테일 / 맨처음 패키지 상세 갈시)
    private PackageScheduleDetails packageScheduleDetails;

    //성인 가격 변환한것 유류할증료 포함 가격 (fuelSurchargeIncluded)
    private String adultPriceString;
    //아동 가격 변환한것 유류할증료 포함 가격 (fuelSurchargeIncluded)
    private String childPriceString;
    //유아 가격 변환한것
    private String babyPriceString;

    //유류할증료 가격 변환
    private String fuelSurcharge;

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

    //여행 예약 가능한 날짜
    private List<String> availableDateList;

    //캐러셀 이미지 모음
    private List<PackageImage> carouselImg;

    //상세 설명 이미지 모음
    private List<PackageImage> infoImg;

    //캐러셀 이미지 있음
    private boolean carouselImgYes;

    //상세 설명 이미지 있음
    private boolean infoImgYes;

    //캐러셀 이미지 화살표 여부
    private boolean carouselImgOne;

    //로그인 여부
    private boolean loginYes;








}
