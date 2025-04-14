package Goods.Reservation_Trip.dto.HanDto;

import Goods.Reservation_Trip.entity.PackageCategory;
import lombok.*;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HeaderDto {

    //카테고리
    List<PackageCategory> mainList;

    //유저 이름
    String name;

    //로그인 상태여부
    boolean loginYes;



}
