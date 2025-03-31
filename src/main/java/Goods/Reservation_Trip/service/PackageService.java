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

    // 저장
    public void save(PackageRequestDto requestDto) {

        Package aPackage = Package.builder()
                .id(requestDto.getId())
                .packageName(requestDto.getPackageName())
                .maximumMember(requestDto.getMaximumSeats())
                .minimumRequired(requestDto.getMinimumRequired())
                .adultPrice(requestDto.getAdultPrice())
                .childPrice(requestDto.getChildPrice())
                .babyPrice(requestDto.getBabyPrice())
                .fuelSurcharge(requestDto.getFuelSurcharge())
                .fuelSurchargeIncluded(requestDto.getFuelSurchargeIncluded())
                .description(requestDto.getDescription())
                .packageCategory(requestDto.getPackageCategory())
                .hotelName(requestDto.getHotelName())
                .guide(requestDto.isGuide())
                .airFare(requestDto.isAirFare())
                .hotelFee(requestDto.isHotelFee())
                .shopping(requestDto.isShopping())
//                .mainImage((PackageImage) requestDto.getMainImage())
                .build();

        if (requestDto.getMainImage() != null && !requestDto.getMainImage().isEmpty()) {
            PackageImage mainPackageImage = packageImageService.create(requestDto, aPackage);
            aPackage.setMainImage(mainPackageImage);
        }


        packageRepository.save(aPackage);
    }
}
