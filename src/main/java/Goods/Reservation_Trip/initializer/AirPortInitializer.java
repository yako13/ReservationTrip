package Goods.Reservation_Trip.initializer;

import Goods.Reservation_Trip.entity.Airport;
import Goods.Reservation_Trip.repository.aPackage.AirportRepository;
import Goods.Reservation_Trip.repository.aPackage.PackageCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Order(2)
public class AirPortInitializer implements CommandLineRunner {

    private final AirportRepository airportRepository;

    private final PackageCategoryRepository packageCategoryRepository;

    @Override
    public void run(String... Args) throws Exception {

        // 국내 극제 공항
        if (!airportRepository.existsByCode("GMP")) {
            airportRepository.save(new Airport(null, "GMP", "김포국제공항", null));
        }
        if (!airportRepository.existsByCode("ICN")) {
            airportRepository.save(new Airport(null, "ICN", "인천국제공항", null));
        }
        if (!airportRepository.existsByCode("TAE")) {
            airportRepository.save(new Airport(null, "TAE", "대구국제공항", null));
        }
        if (!airportRepository.existsByCode("PUS")) {
            airportRepository.save(new Airport(null, "PUS", "부산국제공항", null));
        }

        // 해외 국제 공항
        // 카테고리에 해당이름이 있으면 연결
        packageCategoryRepository.findByName("도쿄").ifPresent(tokyo -> {

            if (!airportRepository.existsByCode("NRT")) {
                airportRepository.save(new Airport(null, "NRT", "나리타국제공항", tokyo));
            }
            if (!airportRepository.existsByCode("HND")) {
                airportRepository.save(new Airport(null, "HND", "하네다국제공항", tokyo));
            }
        });

        packageCategoryRepository.findByName("오사카").ifPresent(osaka -> {

            if (!airportRepository.existsByCode("KIX")) {
                airportRepository.save(new Airport(null, "KIX", "간사이국제공항", osaka));
            }
        });
    }
}
