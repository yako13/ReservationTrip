package Goods.Reservation_Trip.dto.aPackage.req;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PackageScheduleListRequestDto {
    private List<PackageScheduleRequestDto> schedules = new ArrayList<>();
}
