package Goods.Reservation_Trip.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "dib")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Dib {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dib_id")
    private Long id;

    @JoinColumn(name = "member_id", nullable = false)
    @ManyToOne
    private Member member;

    @JoinColumn(name = "package_id", nullable = false)
    @ManyToOne
    private Package packageEntity;
}
