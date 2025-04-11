package Goods.Reservation_Trip.service.aPackage;

import Goods.Reservation_Trip.dto.aPackage.req.PackageOptionRequestDto;
import Goods.Reservation_Trip.entity.Package;
import Goods.Reservation_Trip.entity.PackageOption;
import Goods.Reservation_Trip.repository.aPackage.PackageOptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PackageOptionService {

    private final PackageOptionRepository packageOptionRepository;


    @Transactional
    public void save(Package aPackage, PackageOptionRequestDto requestDto) {
        PackageOption packageOption = PackageOption.builder()
                .aPackage(aPackage)
                .guide(requestDto.isGuide())
                .hotelFee(requestDto.isHotelFee())
                .airfare(requestDto.isAirFare())
                .noShopping(requestDto.isShopping())
                .build();

        packageOptionRepository.save(packageOption);
    }
}
