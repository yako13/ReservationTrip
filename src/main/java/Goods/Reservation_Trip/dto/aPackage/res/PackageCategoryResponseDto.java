package Goods.Reservation_Trip.dto.aPackage.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PackageCategoryResponseDto {

    //대분류
    String major;

    //중분류
    List<String> medium;

    //소분류
    List<List<PackageAirportResponseDto>> sub;
}
