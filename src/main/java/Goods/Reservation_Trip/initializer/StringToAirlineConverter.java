package Goods.Reservation_Trip.initializer;

import Goods.Reservation_Trip.entity.Airline;
import Goods.Reservation_Trip.repository.aPackage.AirlineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StringToAirlineConverter implements Converter<String, Airline> {

    private final AirlineRepository airlineRepository;

    @Override
    public Airline convert(String code) {
        try{
            Long id = Long.valueOf(code);
            return airlineRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("invalid airline id" + id));
        } catch (NumberFormatException e){
        return airlineRepository.findByCode(code)
                .orElseThrow(() -> new IllegalArgumentException("Invalid airline code" + code));
        }
    }
}
