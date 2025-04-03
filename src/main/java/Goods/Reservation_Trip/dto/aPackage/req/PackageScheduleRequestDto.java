package Goods.Reservation_Trip.dto.aPackage.req;

import Goods.Reservation_Trip.enums.Airline;
import Goods.Reservation_Trip.enums.ArrivalPoint;
import Goods.Reservation_Trip.enums.DeparturePoint;
import Goods.Reservation_Trip.enums.PackageStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PackageScheduleRequestDto {

    private Long id;
    /**
     * 여행 기간
     */
    private int period;

    /**
     * 출국 항공사명
     */
    @Enumerated(EnumType.STRING)
    private Airline airlineOut;

    /**
     * 귀국 항공사명
     */
    @Enumerated(EnumType.STRING)
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
    @Enumerated(EnumType.STRING)
    private PackageStatus packageStatus;

    /**
     * 출국 일자
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate departureDateOut;

    /**
     * 출국 도착 일자
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate arrivalDateOut;

    /**
     * 귀국 일자
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate departureDateReturn;

    /**
     * 귀국 도착 일자
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate arrivalDateReturn;

    /**
     * 출국 출발지
     */
    @Enumerated(EnumType.STRING)
    private DeparturePoint departurePointOut;

    /**
     * 출국 도착지
     */
    @Enumerated(EnumType.STRING)
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
    @Enumerated(EnumType.STRING)
    private DeparturePoint departurePointReturn;

    /**
     * 귀국 도착지
     */
    @Enumerated(EnumType.STRING)
    private ArrivalPoint arrivalPointReturn;

    /**
     * 귀국 출발 시간
     */
    @DateTimeFormat(pattern = "HH:mm")
    @JsonFormat(pattern = "HH:mm")
    private LocalTime departureTimeReturn;

    /**
     * 귀국 도착 시간
     */
    @DateTimeFormat(pattern = "HH:mm")
    @JsonFormat(pattern = "HH:mm")
    private LocalTime arrivalTimeReturn;
}
