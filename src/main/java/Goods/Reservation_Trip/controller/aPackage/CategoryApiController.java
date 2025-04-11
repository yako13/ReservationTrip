package Goods.Reservation_Trip.controller.aPackage;

import Goods.Reservation_Trip.entity.PackageCategory;
import Goods.Reservation_Trip.service.aPackage.PackageCategoryAndAirportService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CategoryApiController {

    private final PackageCategoryAndAirportService packageCategoryAndAirportService;

    // 대분류 목록 반환
    @GetMapping("/api/categories")
    public List<PackageCategory> getMainCategories() {
        return packageCategoryAndAirportService.getMainCategories();
    }
}
