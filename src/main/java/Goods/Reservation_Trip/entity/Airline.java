package Goods.Reservation_Trip.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Table(name = "airline")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
public class Airline {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "airline_id")
    private Long id;

    @Column(nullable = false)
    @Comment("항공사 코드")
    private String code;

    @Column(nullable = false)
    @Comment("항공사 명")
    private String name;
}
