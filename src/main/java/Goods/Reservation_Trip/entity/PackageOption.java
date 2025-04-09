package Goods.Reservation_Trip.entity;

import Goods.Reservation_Trip.base.BaseTime;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Table(name = "package_option")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class PackageOption extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "package_id")
    @ManyToOne
    private Package aPackage;

    @Column(nullable = false, columnDefinition = "BOOLEAN")
    @Comment("가이드 동반 여부")
    private boolean guide;

    @Column(name = "hotel_fee", nullable = false, columnDefinition = "BOOLEAN")
    @Comment("호텔비 포함 여부")
    private boolean hotelFee;

    @Column(nullable = false, columnDefinition = "BOOLEAN")
    @Comment("항공료 포함 여부")
    private boolean airfare;

    @Column(name = "no_shopping",nullable = false, columnDefinition = "BOOLEAN")
    @Comment("쇼핑 여부")
    private boolean noShopping;
}
