package Goods.Reservation_Trip.dto.aPackage.req;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PackageCategoryDto {

    //카테고리명
    private String categoryName;

    //분류
    private int depth;

    //상위분류이름
    private String parentsName;
}
