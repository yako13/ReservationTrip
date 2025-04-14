package Goods.Reservation_Trip.dto.aPackage.res;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AdminPackageEditDto {

    private PackageEditResponseDto aPackage;

    private List<PackageScheduleEditResponseDto> packageSchedule;

    private PackageOptionEditResponseDto packageOption;
}
