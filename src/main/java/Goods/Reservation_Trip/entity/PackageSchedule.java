package Goods.Reservation_Trip.entity;

import Goods.Reservation_Trip.enums.PackageStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.util.Date;

@Table(name = "package_schedule")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class PackageSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "package_schedule_id")
    private Long id;

    @JoinColumn(name = "package_id")
    @ManyToOne
    private Package aPackage;

    @Column(name = "departure_date", nullable = false)
    @Comment("출발 날짜")
    private Date departureDate;

    @Column(name = "return_date", nullable = false)
    @Comment("귀국 날짜")
    private Date returnDate;

    @Column(name = "seats_total", nullable = false)
    @Comment("총 좌석 수")
    private int seatsTotal;

    @Column(name = "seats_available", nullable = false)
    @Comment("남은 좌석 수")
    private int seatsAvailable;

    @Enumerated(EnumType.STRING)
    @Column(name = "package_status", nullable = false)
    @Comment("예약 가능 상태")
    private PackageStatus packageStatus;
}
