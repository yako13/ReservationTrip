package Goods.Reservation_Trip.entity;

import Goods.Reservation_Trip.base.BaseTime;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class ReviewImage extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "package_image_id")
    private Long id;

    @JoinColumn(name = "review_id", nullable = false)
    @ManyToOne(cascade = CascadeType.ALL)
    private Review review;

    @Column(name = "original_name", nullable = false)
    @Comment("이미지 원본명")
    private String originalName;

    @Column(nullable = false)
    @Comment("이미지 UUID")
    private UUID uuid;

    @Column(name = "file_extension", nullable = false)
    @Comment("이미지 확장자")
    private String fileExtension;

    @Transient
    public String getImageFullName() {
        return this.uuid.toString() + this.fileExtension;
    }
}
