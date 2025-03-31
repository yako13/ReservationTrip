package Goods.Reservation_Trip.entity;

import Goods.Reservation_Trip.enums.Airline;
import Goods.Reservation_Trip.enums.ArrivalPoint;
import Goods.Reservation_Trip.enums.DeparturePoint;
import Goods.Reservation_Trip.enums.PackageStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

@Table(name = "package_schedule")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class PackageSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "package_schedule_id")
    private Long id;

    @JoinColumn(name = "package_id")
    @ManyToOne(cascade = CascadeType.ALL)
    private Package aPackage;

    @Column(name = "departure_date_out", nullable = true)
    @Comment("여행 출국 날짜")
    private LocalDate departureDateOut;

    @Column(name = "arrival_date_out", nullable = true)
    @Comment("여행 출국 도착 날짜")
    private LocalDate arrivalDateOut;

    @Column(name = "departure_date_return", nullable = true)
    @Comment("여행 귀국 날짜")
    private LocalDate departureDateReturn;

    @Column(name = "arrival_date_return", nullable = true)
    @Comment("여행 귀국 도착 날짜")
    private LocalDate arrivalDateReturn;

    @Enumerated(EnumType.STRING)
    @Column(name = "departure_point_out", nullable = true, columnDefinition = "VARCHAR(50)")
    @Comment("출국 비행기 출발지")
    private DeparturePoint departurePointOut;

    @Enumerated(EnumType.STRING)
    @Column(name = "arrival_point_out", nullable = true, columnDefinition = "VARCHAR(50)")
    @Comment("출국 비행기 도착지")
    private ArrivalPoint arrivalPointOut;

    @Enumerated(EnumType.STRING)
    @Column(name = "departure_point_return", nullable = true, columnDefinition = "VARCHAR(50)")
    @Comment("귀국 비행기 출발지")
    private DeparturePoint departurePointReturn;

    @Enumerated(EnumType.STRING)
    @Column(name = "arrival_point_return", nullable = true, columnDefinition = "VARCHAR(50)")
    @Comment("귀국 비행기 도착지")
    private ArrivalPoint arrivalPointReturn;

    @Column(nullable = false)
    @Comment("여행 기간??? 3박 4일???")
    private String period;

    @Enumerated(EnumType.STRING)
    @Column(name = "air_line_out", nullable = true, columnDefinition = "VARCHAR(50)")
    @Comment("출국 항공사 명")
    private Airline airlineOut;

    @Enumerated(EnumType.STRING)
    @Column(name = "air_line_return", nullable = true, columnDefinition = "VARCHAR(50)")
    @Comment("귀국 항공사 명")
    private Airline airlineReturn;

    /**
     *
     */
    @Column(name = "flight_number_out", nullable = true)
    @Comment("출국 항공편 번호")
    private String flightNumberOut;

    @Column(name = "flight_number_return", nullable = true)
    @Comment("귀국 항공편 번호")
    private String flightNumberReturn;

    /**
     *
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "package_status", nullable = true, columnDefinition = "VARCHAR(50)")
    @Comment("예약 가능 상태")
    private PackageStatus packageStatus;

    @Column(name = "departure_time_out")
    @Comment("출국 출발 시간")
    private LocalTime departureTimeOut;

    @Column(name = "arrival_time_out")
    @Comment("출국 도착 시간")
    private LocalTime arrivalTimeOut;

    @Column(name = "departure_time_return", nullable = true)
    @Comment("귀국 출발 시간")
    private LocalTime departureTimeReturn;

    @Column(name = "arrival_time_return", nullable = true)
    @Comment("귀국 도착 시간")
    private LocalTime arrivalTimeReturn;
}
