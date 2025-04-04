package Goods.Reservation_Trip.entity;

import Goods.Reservation_Trip.base.BaseTime;
import Goods.Reservation_Trip.enums.Airline;
import Goods.Reservation_Trip.enums.ArrivalPoint;
import Goods.Reservation_Trip.enums.DeparturePoint;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.time.LocalTime;

@Table(name = "package_schedule_details")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@Entity
public class PackageScheduleDetails extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "package_schedule_details_id")
    private Long id;

    @JoinColumn(name = "package_schedule_id")
    @ManyToOne
    private PackageSchedule packageSchedule;

    @Enumerated(EnumType.STRING)
    @Column(name = "airline_out", nullable = true, columnDefinition = "VARCHAR(50)")
    @Comment("출국 항공사 명")
    private Airline airlineOut;

    @Enumerated(EnumType.STRING)
    @Column(name = "airline_return", nullable = true, columnDefinition = "VARCHAR(50)")
    @Comment("귀국 항공사 명")
    private Airline airlineReturn;

    @Column(name = "flight_number_out", nullable = true)
    @Comment("출국 항공편 번호")
    private String flightNumberOut;

    @Column(name = "flight_number_return", nullable = true)
    @Comment("귀국 항공편 번호")
    private String flightNumberReturn;

    @Enumerated(EnumType.STRING)
    @Column(name = "departure_point_out", nullable = true, columnDefinition = "VARCHAR(50)")
    @Comment("출국 비행기 출발지")
    private DeparturePoint departurePointOut;

    @Enumerated(EnumType.STRING)
    @Column(name = "arrival_point_out", nullable = true, columnDefinition = "VARCHAR(50)")
    @Comment("출국 비행기 도착지")
    private ArrivalPoint arrivalPointOut;

    @Column(name = "departure_time_out")
    @Comment("출국 출발 시간")
    private LocalTime departureTimeOut;

    @Column(name = "arrival_time_out")
    @Comment("출국 도착 시간")
    private LocalTime arrivalTimeOut;

    @Enumerated(EnumType.STRING)
    @Column(name = "departure_point_return", nullable = true, columnDefinition = "VARCHAR(50)")
    @Comment("귀국 비행기 출발지")
    private DeparturePoint departurePointReturn;

    @Enumerated(EnumType.STRING)
    @Column(name = "arrival_point_return", nullable = true, columnDefinition = "VARCHAR(50)")
    @Comment("귀국 비행기 도착지")
    private ArrivalPoint arrivalPointReturn;

    @Column(name = "departure_time_return", nullable = true)
    @Comment("귀국 출발 시간")
    private LocalTime departureTimeReturn;

    @Column(name = "arrival_time_return", nullable = true)
    @Comment("귀국 도착 시간")
    private LocalTime arrivalTimeReturn;
}
