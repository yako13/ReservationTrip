package Goods.Reservation_Trip.service.aPackage;

import Goods.Reservation_Trip.dto.aPackage.PackageRequestDto;
import Goods.Reservation_Trip.entity.Package;
import Goods.Reservation_Trip.entity.PackageCategory;
import Goods.Reservation_Trip.entity.PackageImage;
import Goods.Reservation_Trip.enums.PackageStatus;
import Goods.Reservation_Trip.repository.aPackage.PackageCategoryRepository;
import Goods.Reservation_Trip.repository.aPackage.PackageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PackageService {

    private final PackageRepository packageRepository;

    private final PackageImageService packageImageService;

    private final PackageScheduleService packageScheduleService;

    private final PackageOptionService packageOptionService;

    private final PackageCategoryRepository packageCategoryRepository;

    // 저장
    @Transactional
    public void save(PackageRequestDto requestDto) {

        PackageCategory mainCategory = packageCategoryRepository.findById(requestDto.getMainCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("대분류 카테고리를 찾을 수 없습니다."));
        PackageCategory subCategory = packageCategoryRepository.findById(requestDto.getSubCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("중분류 카테고리를 찾을 수 없습니다."));
        PackageCategory smallCategory = packageCategoryRepository.findById(requestDto.getSmallCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("소분류 카테고리를 찾을 수 없습니다."));

        Package aPackage = Package.builder()
                .packageName(requestDto.getPackageName())
                .maximumMember(requestDto.getMaximumMember())
                .minimumRequired(requestDto.getMinimumRequired())
                .adultPrice(requestDto.getAdultPrice())
                .childPrice(requestDto.getChildPrice())
                .babyPrice(requestDto.getBabyPrice())
                .fuelSurcharge(requestDto.getFuelSurcharge())
                .fuelSurchargeIncluded(requestDto.getFuelSurchargeIncluded())
                .description(requestDto.getDescription())
                .hotelName(requestDto.getHotelName())
                .mainCategory(mainCategory)
                .subCategory(subCategory)
                .smallCategory(smallCategory)
                .packageStatus(PackageStatus.AVAILABLE)
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
