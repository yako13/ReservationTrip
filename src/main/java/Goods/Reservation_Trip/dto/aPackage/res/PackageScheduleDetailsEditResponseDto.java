package Goods.Reservation_Trip.dto.aPackage.res;

import Goods.Reservation_Trip.dto.aPackage.req.AirlineDto;
import Goods.Reservation_Trip.dto.aPackage.req.AirportDto;
import Goods.Reservation_Trip.entity.Airline;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PackageScheduleDetailsEditResponseDto {

    private Long id;

    /**
     * 출국 항공사명
     */
    private AirlineDto airlineOutId;

    /**
     * 귀국 항공사명
     */
    private AirlineDto airlineReturnId;

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
    private AirportDto departurePointOutId;

    /**
     * 출국 도착지
     */
    private AirportDto arrivalPointOutId;

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
    private AirportDto departurePointReturnId;

    /**
     * 귀국 도착지
     */
    private AirportDto arrivalPointReturnId;

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
