package Goods.Reservation_Trip.entity;

import Goods.Reservation_Trip.base.BaseTime;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Setter
public class Notification extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment(value = "알림 PK")
    @Column(name = "notification_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name="member_id",nullable = false)
    @Comment("회원 PK")
    private Member member;

    @ManyToOne
    @JoinColumn(name="reservation_id",nullable = false)
    @Comment("예약 PK")
    private Reservation reservation;

    @Comment(value = "내용")
    @Column(name = "content")
    private String content;

    @Column(nullable = false)
    @Comment(value = "읽음 여부")
    private boolean isRead;

}
