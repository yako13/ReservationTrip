package Goods.Reservation_Trip.entity;

import Goods.Reservation_Trip.base.BaseTime;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Review extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment(value = "리뷰 PK")
    @Column(name = "review_id")
    private Long id;

    @OneToOne
    @JoinColumn(name="package_id",nullable = false)
    @Comment("패키지 PK")
    private Package aPackage;

    @ManyToOne
    @JoinColumn(name="member_id",nullable = false)
    @Comment("회원 PK")
    private Member member;

    @Column(name="score",nullable = false)
    @Comment("별점")
    private int score;

    @Column(name = "content",nullable = false,length = 500)
    @Comment("리뷰 내용")
    private String content;

    @OneToMany(mappedBy = "review",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReviewImage> reviewImageList = new ArrayList<>();

}
