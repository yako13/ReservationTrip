package Goods.Reservation_Trip.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class PackageImageUrlDto {

    private final String mainImageUrl;

    private final List<String> subImageUrl;

    private final List<String> descImageUrl;
}
