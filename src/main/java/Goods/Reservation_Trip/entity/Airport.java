package Goods.Reservation_Trip.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

import java.util.ArrayList;
import java.util.List;

@Table(name = "airport")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
@Builder
@Setter
public class Airport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "airport_id")
    private Long id;

    @Column(nullable = false)
    @Comment("공항 코드")
    private String code;

    @Column(nullable = false)
    @Comment("공항 이름")
    private String name;

    @ManyToMany
    @JoinTable(
            name = "category_airport",
            joinColumns = @JoinColumn(name = "airport_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private List<PackageCategory> categoryList = new ArrayList<>();


}
