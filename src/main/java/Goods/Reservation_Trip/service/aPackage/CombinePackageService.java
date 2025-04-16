package Goods.Reservation_Trip.service.aPackage;

import Goods.Reservation_Trip.config.ImageManager;
import Goods.Reservation_Trip.dto.aPackage.res.AdminPackageListResponseDto;
import Goods.Reservation_Trip.entity.Package;
import Goods.Reservation_Trip.entity.PackageSchedule;
import Goods.Reservation_Trip.repository.aPackage.PackageScheduleDetailsCustomRepository;
import Goods.Reservation_Trip.util.Formatter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CombinePackageService {

    private final PackageScheduleDetailsCustomRepository packageScheduleDetailsCustomRepository;

    private final ImageManager imageManager;

    // 관리자 패키지 리스트 조회
    public Page<AdminPackageListResponseDto> getAdminPackageAndScheduleList(int page, int size, Long mainCategoryId, Long subCategoryId, Long smallCategoryId, String sort) {
        Pageable pageable = PageRequest.of(page, size);

        return packageScheduleDetailsCustomRepository.findByCategoryAndSubCategories(mainCategoryId, subCategoryId, smallCategoryId, sort, pageable).map(packageScheduleDetails -> {
            String packageMainImagePath = null;
            Package aPackage = packageScheduleDetails.getPackageSchedule().getAPackage();
            PackageSchedule packageSchedule = packageScheduleDetails.getPackageSchedule();
            if (aPackage.getMainImage() != null) {
                packageMainImagePath = imageManager.createImageUrl(aPackage.getMainImage().getImageFullName());
            }

            return AdminPackageListResponseDto.builder()
                    .id(aPackage.getId())
                    .name(aPackage.getPackageName())
                    .mainImagePath(packageMainImagePath)
                    .fuelSurchargeIncluded(Formatter.BigDecimalFormat(aPackage.getFuelSurchargeIncluded()))
                    .departurePointOut(packageScheduleDetails.getDeparturePointOut().getName())
                    .arrivalPointOut(packageScheduleDetails.getArrivalPointOut().getName())
                    .period(aPackage.getPeriod())
                    .departureDateOut(packageSchedule.getDepartureDateOut())
                    .arrivalDateReturn(packageSchedule.getArrivalDateReturn())
                    .packageStatus(aPackage.getPackageStatus().getName())
                    .createdAt(Formatter.getLocalDate(aPackage.getCreatedAt()))
                    .modifiedAt(Formatter.getLocalDate(aPackage.getModifiedAt()))
                    .build();
        });
    }

    // 관리자 패키지 리스트 검색 이름 기준
    public Page<AdminPackageListResponseDto> getAdminPackageSearchList(int page, int size, String name, String sort) {
        Pageable pageable = PageRequest.of(page, size);
        return packageScheduleDetailsCustomRepository.findAvailableEarliestByPackageNameContaining(name, pageable, sort).map(packageScheduleDetails -> {
            String packageMainImagePath = null;
            Package aPackage = packageScheduleDetails.getPackageSchedule().getAPackage();
            PackageSchedule packageSchedule = packageScheduleDetails.getPackageSchedule();
            if (aPackage.getMainImage() != null) {
                packageMainImagePath = imageManager.createImageUrl(aPackage.getMainImage().getImageFullName());
            }
            return AdminPackageListResponseDto.builder()
                    .id(aPackage.getId())
                    .name(aPackage.getPackageName())
                    .mainImagePath(packageMainImagePath)
                    .fuelSurchargeIncluded(Formatter.BigDecimalFormat(aPackage.getFuelSurchargeIncluded()))
                    .departurePointOut(packageScheduleDetails.getDeparturePointOut().getName())
                    .arrivalPointOut(packageScheduleDetails.getArrivalPointOut().getName())
                    .period(aPackage.getPeriod())
                    .departureDateOut(packageSchedule.getDepartureDateOut())
                    .arrivalDateReturn(packageSchedule.getArrivalDateReturn())
                    .createdAt(Formatter.getLocalDate(aPackage.getCreatedAt()))
                    .modifiedAt(Formatter.getLocalDate(aPackage.getModifiedAt()))
                    .build();
        });
    }
}
