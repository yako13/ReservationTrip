package Goods.Reservation_Trip.service.aPackage;

import Goods.Reservation_Trip.dto.aPackage.req.PackageOptionRequestDto;
import Goods.Reservation_Trip.dto.aPackage.res.PackageEditResponseDto;
import Goods.Reservation_Trip.dto.aPackage.res.PackageOptionEditResponseDto;
import Goods.Reservation_Trip.entity.Package;
import Goods.Reservation_Trip.entity.PackageOption;
import Goods.Reservation_Trip.repository.aPackage.PackageOptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PackageOptionService {

    private final PackageOptionRepository packageOptionRepository;


    @Transactional
    public void save(Package aPackage, PackageOptionRequestDto requestDto) {

        Optional<PackageOption> existingOption = packageOptionRepository.findByaPackage(aPackage);

        if (existingOption.isPresent()) {
            PackageOption option = existingOption.get();
            option.update(requestDto);
        } else {

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

    public List<PackageOptionEditResponseDto> getPackageOption(Long id) {
        Optional<PackageOption> optPackageOption = packageOptionRepository.findById(id);

        if (optPackageOption.isEmpty()) throw new RuntimeException("해당 상품의 옵션 없음");

        return optPackageOption.stream()
                .map(packageOption -> PackageOptionEditResponseDto.builder()
                        .id(packageOption.getId())
                        .airFare(packageOption.isAirfare())
                        .shopping(packageOption.isNoShopping())
                        .guide(packageOption.isGuide())
                        .hotelFee(packageOption.isHotelFee())
                        .build()
                ).collect(Collectors.toList());
    }
}
