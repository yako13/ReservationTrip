package Goods.Reservation_Trip.entity;

import Goods.Reservation_Trip.base.BaseTime;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Setter
public class Review extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment(value = "리뷰 PK")
    @Column(name = "review_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name="package_id")
    @Comment("패키지 PK")
    private Package aPackage;

    @OneToOne
    @JoinColumn(name="reservation_id",nullable = false)
    @Comment("예약 PK")
    private Reservation reservation;

    @ManyToOne
    @JoinColumn(name="member_id",nullable = false)
    @Comment("회원 PK")
    private Member member;

    @Column(name="rating",nullable = false)
    @Comment("별점")
    private int rating;

    @Column(name = "content",nullable = false,length = 500)
    @Comment("리뷰 내용")
    private String content;

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReviewImage> reviewImageList = new ArrayList<>();

}
