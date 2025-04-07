package Goods.Reservation_Trip.entity;

import Goods.Reservation_Trip.base.BaseTime;
import Goods.Reservation_Trip.enums.Airline;
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
    @Column(name = "airline_out", nullable = false, columnDefinition = "VARCHAR(50)")
    @Comment("출국 항공사 명")
    private Airline airlineOut;

    @Enumerated(EnumType.STRING)
    @Column(name = "airline_return", nullable = false, columnDefinition = "VARCHAR(50)")
    @Comment("귀국 항공사 명")
    private Airline airlineReturn;

    @Column(name = "flight_number_out", nullable = false)
    @Comment("출국 항공편 번호")
    private String flightNumberOut;

    @Column(name = "flight_number_return", nullable = false)
    @Comment("귀국 항공편 번호")
    private String flightNumberReturn;

    @Column(name = "departure_point_out", nullable = false)
    @Comment("출국 비행기 출발지")
    private String departurePointOut;

    @Column(name = "arrival_point_out", nullable = false)
    @Comment("출국 비행기 도착지")
    private String arrivalPointOut;

    @Column(name = "departure_time_out")
    @Comment("출국 출발 시간")
    private LocalTime departureTimeOut;

    @Column(name = "arrival_time_out")
    @Comment("출국 도착 시간")
    private LocalTime arrivalTimeOut;

    @Column(name = "departure_point_return", nullable = false)
    @Comment("귀국 비행기 출발지")
    private String departurePointReturn;

    @Column(name = "arrival_point_return", nullable = false)
    @Comment("귀국 비행기 도착지")
    private String arrivalPointReturn;

    @Column(name = "departure_time_return", nullable = false)
    @Comment("귀국 출발 시간")
    private LocalTime departureTimeReturn;

    @Column(name = "arrival_time_return", nullable = false)
    @Comment("귀국 도착 시간")
    private LocalTime arrivalTimeReturn;
}
