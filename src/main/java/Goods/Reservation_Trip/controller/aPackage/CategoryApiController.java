package Goods.Reservation_Trip.controller.aPackage;

import Goods.Reservation_Trip.entity.PackageCategory;
import Goods.Reservation_Trip.service.aPackage.PackageCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CategoryApiController {

    private final PackageCategoryService packageCategoryService;

    // 대분류 목록 반환
    @GetMapping("/api/categories")
    public List<PackageCategory> getMainCategories() {
        return packageCategoryService.getMainCategories();
    }
}
