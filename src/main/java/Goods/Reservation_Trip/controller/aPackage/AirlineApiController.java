package Goods.Reservation_Trip.controller.aPackage;

import Goods.Reservation_Trip.dto.aPackage.req.AirlineDto;
import Goods.Reservation_Trip.repository.aPackage.AirlineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class AirlineApiController {

    private final AirlineRepository airlineRepository;

    @GetMapping("/api/airlines")
    public List<AirlineDto> getAirlines(){
        return airlineRepository.findAll().stream()
                .map(a -> new AirlineDto(a.getId(), a.getCode(), a.getName()))
                .collect(Collectors.toList());
    }
}
