package Goods.Reservation_Trip.entity;

import Goods.Reservation_Trip.base.BaseTime;
import Goods.Reservation_Trip.enums.ReservationState;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;


@Table(name = "reservation")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Reservation extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_id")
    private Long id;

    @Column(name="reservation_code")
    @Comment("예약번호")
    private String code;

    @JoinColumn(name = "package_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private Package aPackage;

    @JoinColumn(name = "member_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private Member member;

    @Column(name = "start_date", nullable = false)
    @Comment("출발일자")
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    @Comment("도착일자")
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "reservation_state", nullable = false, columnDefinition = "VARCHAR(50)")
    @Comment("예약 상태")
    private ReservationState reservationState;

    @Column(nullable = false)
    @Comment("카카오페이 고유 번호")
    private String aid;

    @Column(nullable = false)
    @Comment("카카오페이 결제 고유 번호")
    private String tid;

    @Column(name = "pay_type", nullable = false)
    @Comment("결제 타입 (현금, 카드)")
    private String payType;

    @Column(name = "issuer_corp")
    @Comment("카카오페이 발급사명")
    private String issuerCorp;

    @Column(name = "pur_corp", nullable = false)
    @Comment("카카오페이 매입사명")
    private String purCorp;

    @Column(name = "total_pay", nullable = false)
    @Comment("총 결제 금액")
    private BigDecimal totalPay;

    @Column(name = "pur_corp_code", nullable = false)
    @Comment("매입사 코드")
    private String purCorpCode;

    @Column(nullable = false)
    @Comment("카드사 승인 번호")
    private String approved;

}

