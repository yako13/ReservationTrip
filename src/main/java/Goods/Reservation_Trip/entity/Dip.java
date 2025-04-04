package Goods.Reservation_Trip.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "dip")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class Dip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dip_id")
    private Long id;

    @JoinColumn(name = "member_id", nullable = false)
    @ManyToOne
    private Member member;

    @JoinColumn(name = "package_id", nullable = false)
    @ManyToOne
    private Package aPackage;
}
