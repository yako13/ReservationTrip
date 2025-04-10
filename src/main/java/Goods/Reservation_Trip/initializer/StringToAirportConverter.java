package Goods.Reservation_Trip.initializer;

import Goods.Reservation_Trip.entity.Airport;
import Goods.Reservation_Trip.repository.aPackage.AirportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StringToAirportConverter implements Converter<String, Airport> {

    private final AirportRepository airportRepository;

    @Override
    public Airport convert(String code){
        try{
            Long id = Long.valueOf(code);
            return airportRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("invalid airport id" + id));
        } catch (NumberFormatException e){
        return airportRepository.findByCode(code)
                .orElseThrow(() -> new IllegalArgumentException("invalid airport code" + code));
        }
    }
}
