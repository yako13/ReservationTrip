package Goods.Reservation_Trip.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

import java.util.List;

@Table(name = "package_category")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
public class PackageCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "package_category_id")
    private Long id;

    @Comment("카테고리명")
    @Column(nullable = true)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    @Comment("상위 카테고리")
    @JsonIgnore
    private PackageCategory parent;

    private int depth;

    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
    private List<PackageCategory> children;

    public PackageCategory(Long id, String name, PackageCategory parent, int depth) {
        this.id = id;
        this.name = name;
        this.parent = parent;
        this.depth = depth;
    }
}
