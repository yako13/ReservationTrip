package Goods.Reservation_Trip.entity;

import Goods.Reservation_Trip.base.BaseTime;
import Goods.Reservation_Trip.enums.AgeGroup;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Entity(name="reservation_details")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationDetails extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="reservation_details_id")
    private Long id;

    @JoinColumn(name = "reservation_id",nullable = false)
    @ManyToOne
    private Reservation reservation;

    @Column(name="name",nullable = false)
    @Comment("예약 인원 이름")
    private String name;

    @Column(name="birth",nullable = false)
    @Comment("예약 인원 생년월일")
    private String birth;

    @Column(name="gender",nullable = false)
    @Comment("예약 인원 성별, 남1 여0")
    private boolean gender;

    @Column(name="phone_number")
    @Comment("예약 인원 연락처")
    private String phoneNumber;

    @Column(name="age_group",nullable = false)
    @Enumerated(EnumType.STRING)
    @Comment("연령대")
    private AgeGroup ageGroup;

    @Column
    @Comment("여권 번호")
    private String passportNum;

    @Column(name="default_reservation",nullable = false)
    @Comment("예약자 여부, 예약자1 미예약자0")
    private boolean defaultReservation;
}
