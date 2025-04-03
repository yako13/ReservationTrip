package Goods.Reservation_Trip.service.aPackage;

import Goods.Reservation_Trip.config.ImageManager;
import Goods.Reservation_Trip.dto.aPackage.res.AdminPackageListResponseDto;
import Goods.Reservation_Trip.entity.Package;
import Goods.Reservation_Trip.repository.aPackage.PackageScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CombinePackageService {

    private final PackageScheduleRepository packageScheduleRepository;

    private final ImageManager imageManager;

    // 관리자 패키지 리스트 조회
    public Page<AdminPackageListResponseDto> getAdminPackageAndScheduleList(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return packageScheduleRepository.findAll(pageable).map(packageSchedule -> {
            String packageMainImagePath = null;
            Package aPackage = packageSchedule.getAPackage();

            if (aPackage.getMainImage() != null) {
                packageMainImagePath = imageManager.createImageUrl(aPackage.getMainImage().getImageFullName());
            }

            return AdminPackageListResponseDto.builder()
                    .id(aPackage.getId())
                    .name(aPackage.getPackageName())
                    .mainImagePath(packageMainImagePath)
                    .fuelSurchargeIncluded(aPackage.getFuelSurchargeIncluded())
                    .departurePointOut(String.valueOf(packageSchedule.getDeparturePointOut().getName()))
                    .arrivalPointOut(String.valueOf(packageSchedule.getArrivalPointOut().getName()))
                    .period(packageSchedule.getPeriod())
                    .name(packageSchedule.getAPackage().getPackageName())
                    .fuelSurchargeIncluded(packageSchedule.getAPackage().getFuelSurchargeIncluded())
                    .departureDateOut(packageSchedule.getDepartureDateOut())
                    .arrivalDateReturn(packageSchedule.getArrivalDateReturn())
                    .build();
        });
    }
}
