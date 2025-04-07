package Goods.Reservation_Trip.dto.aPackage.req;

import Goods.Reservation_Trip.entity.Airline;
import Goods.Reservation_Trip.entity.Airport;
import com.fasterxml.jackson.annotation.JsonFormat;
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
     * 출국 출발지
     */
    private Airport departurePointOut;

    /**
     * 출국 도착지
     */
    private Airport arrivalPointOut;

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
    private String departurePointReturn;

    /**
     * 귀국 도착지
     */
    private String arrivalPointReturn;

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
