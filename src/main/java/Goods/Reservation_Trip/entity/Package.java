package Goods.Reservation_Trip.entity;

import Goods.Reservation_Trip.base.BaseTime;
import Goods.Reservation_Trip.enums.Airline;
import Goods.Reservation_Trip.enums.PackageCategory;
import Goods.Reservation_Trip.enums.StartingPoint;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Table(name = "package")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Package extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "package_id")
    private Long id;

    @Column(name = "package_name", nullable = false)
    @Comment(value = "여행지 상품명")
    private String packageName;

    @Column(name = "maximum_member", nullable = false)
    @Comment("예약가능 최대 인원")
    private int maximumMember;

    @Column(name = "adult_price", nullable = false)
    @Comment("성인 인당 가격")
    private BigDecimal adultPrice;

    @Column(name = "child_price", nullable = false)
    @Comment("아동 인당 가격")
    private BigDecimal childPrice;

    @Column(name = "baby_price", nullable = false)
    @Comment("유아 인당 가격")
    private BigDecimal babyPrice;

    @Column(name = "adult_number", nullable = false)
    @Comment("성인 인원수")
    private int adultNumber;

    @Column(name = "child_number", nullable = false)
    @Comment("아동 인원수")
    private int childNumber;

    @Column(name = "baby_number", nullable = false)
    @Comment("아동 인원수")
    private int babyNumber;

    @Comment("대표 이미지")
    @OneToOne
    @JoinColumn(name = "main_image", nullable = false)
    private PackageImage mainImage;

    @Column(name = "package_category", nullable = false)
    @Comment("카테고리")
    private PackageCategory packageCategory;

    @Column(name = "start_date", nullable = false)
    @Comment("여행 시작일")
    private LocalDate startDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "starting_point", nullable = false, columnDefinition = "VARCHAR(50)")
    @Comment("출발지")
    private StartingPoint startingPoint;

    @Column(nullable = false)
    @Comment("가이드 동반 여부")
    private boolean guide;

    @Column(name = "hotel_fee", nullable = false)
    @Comment("호텔비 포함 여부")
    private boolean hotelFee;

    @Column(nullable = false)
    @Comment("항공료 포함 여부")
    private boolean airfare;

    @Column(nullable = false)
    @Comment("여행 기간")
    private String period;

    @Column(name = "air_line", nullable = false)
    @Comment("항공사 명")
    private Airline airline;

    @Column(name = "hotel_name", nullable = false)
    @Comment("호텔 명")
    private String hotelName;

    @JoinColumn(name = "package_image", nullable = false)
    @OneToOne
    @Comment("일정 사진")
    private PackageImage scheduleImage;

    @OneToMany(mappedBy = "aPackage", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PackageImage> packageImages;

    @OneToMany(mappedBy = "aPackage", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reservation> reservations;
}
