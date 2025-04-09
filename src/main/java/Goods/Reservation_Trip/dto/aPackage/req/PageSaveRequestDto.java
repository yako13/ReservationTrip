package Goods.Reservation_Trip.dto.aPackage.req;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PageSaveRequestDto {

    private PackageRequestDto aPackage;

    private List<PackageScheduleRequestDto> packageSchedule;

    private PackageOptionRequestDto packageOption;
}
