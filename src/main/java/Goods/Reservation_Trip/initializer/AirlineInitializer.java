package Goods.Reservation_Trip.initializer;

import Goods.Reservation_Trip.entity.Airline;
import Goods.Reservation_Trip.repository.aPackage.AirlineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AirlineInitializer implements CommandLineRunner {

    private final AirlineRepository airlineRepository;

    @Override
    public void run(String...args) {

        if (!airlineRepository.existsByCode("ASIANA_AIR")) {
            airlineRepository.save(new Airline(null, "ASIANA_AIR", "아시아나항공"));
        }
        if (!airlineRepository.existsByCode("KOREAN_AIR")) {
            airlineRepository.save(new Airline(null, "KOREAN_AIR", "대한항공"));
        }
        if (!airlineRepository.existsByCode("AIR_SEOUL")) {
            airlineRepository.save(new Airline(null, "AIR_SEOUL", "에어서울"));
        }
        if (!airlineRepository.existsByCode("AIR_BUSAN")) {
            airlineRepository.save(new Airline(null, "AIR_BUSAN", "에어부산"));
        }
        if (!airlineRepository.existsByCode("JEJU_Air")) {
            airlineRepository.save(new Airline(null, "JEJU_AIR", "제주항공"));
        }
    }
}
