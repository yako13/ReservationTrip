package Goods.Reservation_Trip.service;

import Goods.Reservation_Trip.dto.PackageRequestDto;
import Goods.Reservation_Trip.entity.Package;
import Goods.Reservation_Trip.entity.PackageOption;
import Goods.Reservation_Trip.repository.PackageOptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PackageOptionService {

    private final PackageOptionRepository packageOptionRepository;


    public PackageOption save(Package aPackage, PackageRequestDto requestDto) {
        PackageOption packageOption = PackageOption.builder()
                .aPackage(aPackage)
                .guide(requestDto.isGuide())
                .hotelFee(requestDto.isHotelFee())
                .airfare(requestDto.isAirFare())
                .noShopping(requestDto.isShopping())
                .build();

        return packageOptionRepository.save(packageOption);
    }
}
