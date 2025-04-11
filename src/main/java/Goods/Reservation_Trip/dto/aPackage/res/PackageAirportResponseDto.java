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
public class PackageAirportResponseDto {

    //소분류
    private String sub;

    //공항
    private List<String> airport;
}
