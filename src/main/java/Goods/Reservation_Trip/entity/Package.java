package Goods.Reservation_Trip.entity;

import Goods.Reservation_Trip.base.BaseTime;
import Goods.Reservation_Trip.enums.PackageCategory;
import Goods.Reservation_Trip.enums.PackageStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

import java.math.BigDecimal;
import java.util.List;

@Table(name = "package")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
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

    @Column(name = "minimum_required", nullable = false)
    @Comment("최소 예약 필요 인원")
    private int minimumRequired;

    @Column(name = "adult_price", nullable = false)
    @Comment("성인 인당 가격")
    private BigDecimal adultPrice;

    @Column(name = "child_price", nullable = false)
    @Comment("아동 인당 가격")
    private BigDecimal childPrice;

    @Column(name = "baby_price", nullable = false)
    @Comment("유아 인당 가격")
    private BigDecimal babyPrice;

    @Column(nullable = false)
    @Comment("간략한 설명글")
    private String description;

    @OneToOne
    @JoinColumn(name = "main_image", nullable = true)
    @Comment("대표 이미지")
    private PackageImage mainImage;

    @Column(name = "package_category", nullable = true)
    @Comment("카테고리")
    @Enumerated(EnumType.STRING)
    private PackageCategory packageCategory;

    @Column(name = "fuel_surcharge", nullable = false)
    @Comment("유류할증료")
    private BigDecimal fuelSurcharge;

    @Column(name = "fuel_surcharge_included", nullable = false)
    @Comment("유류할증료 포함")
    private BigDecimal fuelSurchargeIncluded;

//    @Column(name = "hotel_name", nullable = false)
//    @Comment("호텔 명")
//    private List<String> hotelName;

    @Enumerated(EnumType.STRING)
    @Column(name = "package_status", nullable = false, columnDefinition = "VARCHAR(50)")
    @Comment("예약 가능 상태")
    private PackageStatus packageStatus;

    @OneToMany(mappedBy = "aPackage", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PackageImage> packageImageList;

    @OneToMany(mappedBy = "aPackage", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reservation> reservationList;

    @OneToMany(mappedBy = "aPackage", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PackageSchedule> packageScheduleList;

}
