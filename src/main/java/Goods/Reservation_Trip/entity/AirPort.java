package Goods.Reservation_Trip.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Table(name = "air_port")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
public class AirPort {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Comment("공항 코드")
    private String code;

    @Column(nullable = false)
    @Comment("공핳 이름")
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private PackageCategory category;

}
