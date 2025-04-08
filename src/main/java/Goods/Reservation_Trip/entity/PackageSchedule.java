package Goods.Reservation_Trip.entity;

import Goods.Reservation_Trip.base.BaseTime;
import Goods.Reservation_Trip.enums.PackageStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

import java.time.LocalDate;
import java.util.List;

@Table(name = "package_schedule")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class PackageSchedule extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "package_schedule_id")
    private Long id;

    @JoinColumn(name = "package_id")
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Package aPackage;

    @Column(name = "departure_date_out", nullable = false)
    @Comment("여행 출국 날짜")
    private LocalDate departureDateOut;

    @Column(name = "arrival_date_out", nullable = false)
    @Comment("여행 출국 도착 날짜")
    private LocalDate arrivalDateOut;

    @Column(name = "departure_date_return", nullable = false)
    @Comment("여행 귀국 날짜")
    private LocalDate departureDateReturn;

    @Column(name = "arrival_date_return", nullable = false)
    @Comment("여행 귀국 도착 날짜")
    private LocalDate arrivalDateReturn;

    @Enumerated(EnumType.STRING)
    @Comment("예약 가능 상태")
    private PackageStatus packageStatus;

    @OneToOne(mappedBy ="packageSchedule", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private PackageScheduleDetails packageScheduleDetails;

    public void setAPackage(Package aPackage) {
        this.aPackage = aPackage;
    }
}
