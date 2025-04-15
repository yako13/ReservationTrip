package Goods.Reservation_Trip.controller.aPackage;

import Goods.Reservation_Trip.dto.aPackage.req.AirportDto;
import Goods.Reservation_Trip.entity.PackageCategory;
import Goods.Reservation_Trip.repository.aPackage.AirportRepository;
import Goods.Reservation_Trip.repository.aPackage.PackageCategoryRepository;
import Goods.Reservation_Trip.service.aPackage.PackageCategoryAndAirportService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/airports")
public class AirportApiController {

    private final AirportRepository airportRepository;

    private final PackageCategoryRepository packageCategoryRepository;

    private final PackageCategoryAndAirportService packageCategoryAndAirportService;

    @GetMapping("/category/{categoryId}")
    public List<AirportDto> getAirportsByCategory(@PathVariable Long categoryId) {
        return airportRepository.findByCategoryId(categoryId)
                .stream()
                .map(air -> new AirportDto(air.getId(), air.getName(), air.getCode()))
                .toList();
    }

    @GetMapping("/category/name/{categoryName}")
    public List<AirportDto> getAirportsByCategoryName(@PathVariable String categoryName) {
        PackageCategory root = packageCategoryRepository.findByName(categoryName)
                .orElseThrow(() -> new RuntimeException("카테고리 없음"));

        List<String> names = packageCategoryAndAirportService.collectCategoryNames(root);
        System.out.println("카테고리 이름 목록: " + names);

        List<AirportDto> result = airportRepository.findAirportsByCategoryNames(names)
                .stream()
                .filter(Objects::nonNull)
                .map(air -> new AirportDto(air.getId(), air.getName(), air.getCode()))
                .toList();

        return result;


    }
}
