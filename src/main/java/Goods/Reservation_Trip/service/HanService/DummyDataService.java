package Goods.Reservation_Trip.service.HanService;

import Goods.Reservation_Trip.entity.Package;
import Goods.Reservation_Trip.entity.*;
import Goods.Reservation_Trip.enums.PackageImageType;
import Goods.Reservation_Trip.enums.PackageStatus;
import Goods.Reservation_Trip.repository.aPackage.AirlineRepository;
import Goods.Reservation_Trip.repository.aPackage.AirportRepository;
import Goods.Reservation_Trip.repository.aPackage.PackageCategoryRepository;
import Goods.Reservation_Trip.repository.aPackage.PackageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class DummyDataService {
    private final PackageRepository packageRepository;
    private final PackageCategoryRepository categoryRepository;
    private final AirlineRepository airlineRepository;
    private final AirportRepository airportRepository;


    public void generateDummyPackages(int count) {
        for (int i = 0; i < count; i++) {
            PackageCategory mainCat = categoryRepository.findById(1L).orElseThrow();
            PackageCategory subCat = categoryRepository.findById(3L).orElseThrow();
            PackageCategory smallCat = categoryRepository.findById(5L).orElseThrow();
            Airline airline = airlineRepository.findById(1L).orElseThrow();
            Airport airportOut = airportRepository.findById(1L).orElseThrow();
            Airport airportIn = airportRepository.findById(2L).orElseThrow();

            UUID uuid = UUID.fromString("55555555-5555-5555-5555-555555555555");

            PackageImage packageImage = PackageImage.builder()
                    .aPackage(null)
                    .packageImageType(PackageImageType.MAIN)
                    .originalName("111.jpg")
                    .uuid(uuid)
                    .fileExtension(".jpg")
                    .build();


            Package pkg = Package.builder()
                    .packageName("더미 패키지 " + (i + 1))
                    .mainImage(packageImage) // 메인 이미지로 설정
                    .averageRating(4.0)
                    .adultPrice(BigDecimal.valueOf(500000))
                    .childPrice(BigDecimal.valueOf(400000))
                    .babyPrice(BigDecimal.valueOf(30000))
                    .description("설명 " + (i + 1))
                    .period(3)
                    .fuelSurcharge(BigDecimal.valueOf(10000))
                    .fuelSurchargeIncluded(BigDecimal.valueOf(510000))
                    .hotelName("호텔 " + (i + 1))
                    .mainCategory(mainCat)
                    .subCategory(subCat)
                    .smallCategory(smallCat)
                    .packageStatus(PackageStatus.AVAILABLE)
                    .build();

            // 리스트 초기화
            pkg.setPackageScheduleList(new ArrayList<>());
            pkg.setReviewList(new ArrayList<>());
            pkg.setPackageImageList(new ArrayList<>());

            // 연관 관계 설정
            packageImage.setAPackage(pkg);
            pkg.getPackageImageList().add(packageImage);


            packageRepository.save(pkg);

            PackageOption option = PackageOption.builder()
                    .aPackage(pkg)
                    .guide(true)
                    .hotelFee(true)
                    .airfare(true)
                    .noShopping(false)
                    .build();

            pkg.setPackageOption(option);

            for (int j = 0; j < 2; j++) {
                LocalDate dep = LocalDate.now().plusDays(i * 5 + j * 3);
                LocalDate ret = dep.plusDays(3);

                PackageSchedule schedule = PackageSchedule.builder()
                        .aPackage(pkg)
                        .maximumMember(20)
                        .minimumRequired(4)
                        .reservedMemberCount(0)
                        .departureDateOut(dep)
                        .arrivalDateOut(dep)
                        .departureDateReturn(ret)
                        .arrivalDateReturn(ret)
                        .packageStatus(j % 2 == 0 ? PackageStatus.AVAILABLE : PackageStatus.FULL)
                        .build();

                PackageScheduleDetails detail = PackageScheduleDetails.builder()
                        .packageSchedule(schedule)
                        .airlineOut(airline)
                        .airlineReturn(airline)
                        .flightNumberOut("7C10" + i + j)
                        .flightNumberReturn("7C20" + i + j)
                        .departurePointOut(airportOut)
                        .arrivalPointOut(airportIn)
                        .departureTimeOut(LocalTime.of(8 + j, 0))
                        .arrivalTimeOut(LocalTime.of(10 + j, 30))
                        .departurePointReturn(airportIn)
                        .arrivalPointReturn(airportOut)
                        .departureTimeReturn(LocalTime.of(18 + j, 0))
                        .arrivalTimeReturn(LocalTime.of(20 + j, 30))
                        .build();

                schedule.setPackageScheduleDetails(detail);
                pkg.getPackageScheduleList().add(schedule);
            }

            packageRepository.save(pkg); // cascade 덕분에 한 번 저장으로 전부 저장됨
        }
    }


}
