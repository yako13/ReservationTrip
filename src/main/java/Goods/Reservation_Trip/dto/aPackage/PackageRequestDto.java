package Goods.Reservation_Trip.dto.aPackage;

import Goods.Reservation_Trip.enums.Airline;
import Goods.Reservation_Trip.enums.ArrivalPoint;
import Goods.Reservation_Trip.enums.DeparturePoint;
import Goods.Reservation_Trip.enums.PackageStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalTime;
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
    private int maximumMember;

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
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private String departureDateOut;

    /**
     * 출국 도착 일자
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private String arrivalDateOut;

    /**
     * 귀국 일자
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private String departureDateReturn;

    /**
     * 귀국 도착 일자
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private String arrivalDateReturn;

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
    @DateTimeFormat(pattern = "HH:mm")
    @JsonFormat(pattern = "HH:mm")
    private LocalTime departureTimeOut;

    /**
     * 출국 도착 시간
     */
    @DateTimeFormat(pattern = "HH:mm")
    @JsonFormat(pattern = "HH:mm")
    private LocalTime arrivalTimeOut;

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
    @DateTimeFormat(pattern = "HH;mm")
    @JsonFormat(pattern = "HH:mm")
    private LocalTime departureTimeReturn;

    /**
     * 귀국 도착 시간
     */
    @DateTimeFormat(pattern = "HH:mm")
    @JsonFormat(pattern = "HH:mm")
    private LocalTime arrivalTimeReturn;

    /**
     * 호텔명 리스트
     */
    private String hotelName;

    /**
     * 대분류 ID
     */
    private Long mainCategoryId;

    /**
     * 중분류 ID
     */
    private Long subCategoryId;

    /**
     * 소분류 ID
     */
    private Long smallCategoryId;

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

    private Long packageScheduleId;
}
