package Goods.Reservation_Trip.config;

import Goods.Reservation_Trip.entity.AirPort;
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
        if (!airportRepository.existsByCode("KIMPO")) {
            airportRepository.save(new AirPort(null, "KIMPO", "김포국제공항", null));
        }
        if (!airportRepository.existsByCode("INCHEON")) {
            airportRepository.save(new AirPort(null, "INCHEON", "인천국제공항", null));
        }
        if (!airportRepository.existsByCode("DEAGU")) {
            airportRepository.save(new AirPort(null, "DEAGU", "대구국제공항", null));
        }
        if (!airportRepository.existsByCode("BUSAN")) {
            airportRepository.save(new AirPort(null, "BUSAN", "부산", null));
        }
        if (!airportRepository.existsByCode("사천")) {
            airportRepository.save(new AirPort(null, "사천", "사천", null));
        }

        // 해외 국제 공항
        packageCategoryRepository.findByName("도쿄").ifPresent(tokyo -> {

            if (!airportRepository.existsByCode("NARITA")) {
                airportRepository.save(new AirPort(null, "NARITA", "나리타국제공항", tokyo));
            }
            if (!airportRepository.existsByCode("HANEDA")) {
                airportRepository.save(new AirPort(null, "HANEDA", "하네다국제공항", tokyo));
            }
        });

        packageCategoryRepository.findByName("오사카").ifPresent(osaka -> {

            if (!airportRepository.existsByCode("KANSAI")) {
                airportRepository.save(new AirPort(null, "KANSAI", "간사이국제공항", osaka));
            }
        });
    }
}
