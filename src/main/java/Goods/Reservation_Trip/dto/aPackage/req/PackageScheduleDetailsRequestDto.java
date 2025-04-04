package Goods.Reservation_Trip.dto.aPackage.req;

import Goods.Reservation_Trip.enums.Airline;
import Goods.Reservation_Trip.enums.ArrivalPoint;
import Goods.Reservation_Trip.enums.DeparturePoint;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PackageScheduleDetailsRequestDto {

    private Long id;

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
