package Goods.Reservation_Trip.dto;

import Goods.Reservation_Trip.enums.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PackageRequestDto {

    private Long id;

    /**
     * 패키지명
     */
    private String packageName;

    /**
     * 최대 에약 가능 인원
     */
    private int maximumSeats;

    /**
     * 출발에 필요한 최소 예약 인원
     */
    private int minimumRequired;

    /**
     * 성인 1인당 금액
     */
    private BigDecimal adultPrice;

    /**
     * 아동 1인당 금액
     */
    private BigDecimal childPrice;

    /**
     * 유아 1인당 금액
     */
    private BigDecimal babyPrice;

    /**
     * 유류할증료
     */
    private BigDecimal fuelSurcharge;

    /**
     * 유류할증료 포함 금액
     */
    private BigDecimal fuelSurchargeIncluded;

    /**
     * 간단 설명글
     */
    private String description;

    /**
     * 카테고리
     */
    private PackageCategory packageCategory;

    /**
     * 메인 이미지
     */
    private MultipartFile mainImage;

    /**
     * 서브 이미지
     */
    private List<MultipartFile> subImage;

    /**
     * 일정 설명 이미지
     */
    private List<MultipartFile> descImage;

    /**
     * 여행 기간
     */
    private String period;

    /**
     * 출국 항공사명
     */
    private Airline airlineOut;

    /**
     * 귀국 항공사명
     */
    private Airline airlineReturn;

    /**
     * 출국 항공기 번호
     */
    private String flightNumberOut;

    /**
     * 귀국 항공기 번호
     */
    private String flightNumberReturn;

    /**
     * 예약 가능 상태
     */
    private PackageStatus packageStatus;

    /**
     * 출국 일자
     */
    private Date departureDate;

    /**
     * 귀국 일자
     */
    private Date returnDate;

    /**
     * 출국 출발지
     */
    private DeparturePoint departurePointOut;

    /**
     * 출국 도착지
     */
    private ArrivalPoint arrivalPointOut;

    /**
     * 출국 출발 시간
     */
    private LocalDateTime departureTimeOut;

    /**
     * 출국 도착 시간
     */
    private LocalDateTime arrivalTimeOut;

    /**
     * 귀국 출발지
     */
    private DeparturePoint departurePointReturn;

    /**
     * 귀국 도착지
     */
    private ArrivalPoint arrivalPointReturn;

    /**
     * 귀국 출발 시간
     */
    private LocalDateTime departureTimeReturn;

    /**
     * 귀국 도착 시간
     */
    private LocalDateTime arrivalTimeReturn;

    /**
     * 호텔명 리스트
     */
    private List<String> hotelName;

    /**
     * 가이드 유무
     */
    private boolean guide;

    /**
     * 항공료 포함 유무
     */
    private boolean airFare;

    /**
     * 호텔비 포함 유무
     */
    private boolean hotelFee;

    /**
     * 쇼핑 유무
     */
    private boolean shopping;
}
