package Goods.Reservation_Trip.service;

import Goods.Reservation_Trip.dto.PackageRequestDto;
import Goods.Reservation_Trip.entity.Package;
import Goods.Reservation_Trip.entity.PackageImage;
import Goods.Reservation_Trip.repository.PackageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PackageService {

    private final PackageRepository packageRepository;

    private final PackageImageService packageImageService;

    private final PackageScheduleService packageScheduleService;

    private final PackageOptionService packageOptionService;

    // 저장
    public void save(PackageRequestDto requestDto) {

        Package aPackage = Package.builder()
                .id(requestDto.getId())
                .packageName(requestDto.getPackageName())
                .maximumMember(requestDto.getMaximumMember())
                .minimumRequired(requestDto.getMinimumRequired())
                .adultPrice(requestDto.getAdultPrice())
                .childPrice(requestDto.getChildPrice())
                .babyPrice(requestDto.getBabyPrice())
                .fuelSurcharge(requestDto.getFuelSurcharge())
                .fuelSurchargeIncluded(requestDto.getFuelSurchargeIncluded())
                .description(requestDto.getDescription())
                .packageCategory(requestDto.getPackageCategory())
                .hotelName(requestDto.getHotelName())
                .build();

        if (requestDto.getMainImage() != null && !requestDto.getMainImage().isEmpty()) {
            PackageImage mainPackageImage = packageImageService.create(requestDto, aPackage);
            aPackage.setMainImage(mainPackageImage);
        }
        // 패키지 기본 정보 저장
        packageRepository.save(aPackage);
        // 항공편 일정 정보 저장
        packageScheduleService.save(aPackage, requestDto);
        // 선택 옵션 저장
        packageOptionService.save(aPackage, requestDto);
    }
}
