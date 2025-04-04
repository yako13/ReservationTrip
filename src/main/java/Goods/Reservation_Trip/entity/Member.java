package Goods.Reservation_Trip.entity;

import Goods.Reservation_Trip.base.BaseTime;
import Goods.Reservation_Trip.enums.MemberRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.time.LocalDateTime;
import java.util.List;

@Table(name = "member")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class Member extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment(value = "회원 PK")
    @Column(name = "member_id")
    private Long id;

    @Column(name = "email", nullable = false)
    @Comment(value = "회원 이메일")
    private String email;

    @Column(nullable = true)
    @Comment(value = "비밀번호")
    private String password;

    @Column(nullable = false)
    @Comment(value = "회원 이름")
    private String name;

    @Column(nullable = true)
    @Comment(value = "생일")
    private String birth;

    @Column(nullable = true)
    @Comment(value = "성별, 남1 여0")
    private boolean gender;

    @Column(name = "phone_number", nullable = true)
    @Comment(value = "휴대전화번호")
    private String phoneNumber;

    @Column(nullable = true)
    @Comment(value = "소셜로그인 제공자")
    private String provider;

    @Column(name = "provider_id", nullable = true)
    @Comment(value = "소셜 로그인 ID")
    private String providerId;

    @Column(name = "withdrawal_at", nullable = true)
    @Comment(value = "탈퇴 날짜")
    private LocalDateTime withdrawalAt;

    @Column(nullable = false, columnDefinition = "VARCHAR(50)")
    @Enumerated(EnumType.STRING)
    @Comment(value = "권한")
    private MemberRole role;

    @Column(name = "terms_agreement", nullable = false)
    @Comment(value = "이용약관 동의 여부, 동의1 미동의0")
    private boolean termsAgreement;

    @Column(name = "privacy_agreement", nullable = false)
    @Comment(value = "개인정보 수집 동의 여부, 동의1 미동의0")
    private boolean privacyAgreement;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reservation> reservationList;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Dib> dibList;

    public void setPassword(String password){
        this.password = password;
    }

    public void editMember(String password,String name,String birth,String phoneNumber){
        this.password = password;
        this.name = name;
        this.birth = birth;
        this.phoneNumber = phoneNumber;
    }

    public void setWithdrawalAt(){
        this.withdrawalAt = LocalDateTime.now();
        this.role = MemberRole.CANCELLATION;
    }
}
