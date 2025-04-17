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

    //대분류
    private String main;

    //중분류
    private String sub;

    //소분류
    private String small;

    //수정할 대분류
    private String mainEdit;

    //수정할 중분류
    private String subEdit;

}
