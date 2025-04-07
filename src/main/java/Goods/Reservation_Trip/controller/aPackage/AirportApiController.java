package Goods.Reservation_Trip.controller.aPackage;

import Goods.Reservation_Trip.dto.aPackage.req.AirportDto;
import Goods.Reservation_Trip.repository.aPackage.AirportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/airports")
public class AirportApiController {

    private final AirportRepository airportRepository;

    @GetMapping("/category/{categoryId}")
    public List<AirportDto> getAirportsByCategory(@PathVariable Long categoryId) {
        return airportRepository.findByCategoryId(categoryId)
                .stream()
                .map(air -> new AirportDto(air.getId(), air.getName()))
                .toList();
    }
}
