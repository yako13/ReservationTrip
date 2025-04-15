package Goods.Reservation_Trip.controller.aPackage;

import Goods.Reservation_Trip.dto.aPackage.res.AdminPackageListResponseDto;
import Goods.Reservation_Trip.entity.PackageCategory;
import Goods.Reservation_Trip.service.aPackage.CombinePackageService;
import Goods.Reservation_Trip.service.aPackage.PackageCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CategoryApiController {

    private final PackageCategoryService packageCategoryService;

    private final CombinePackageService combinePackageService;

    // 대분류 목록 반환
    @GetMapping("/api/categories")
    public List<PackageCategory> getMainCategories() {
        return packageCategoryService.getMainCategories();
    }

    @ResponseBody
    @GetMapping("/admin/package/list/json")
    public Page<AdminPackageListResponseDto> getFilteredPackageListJson(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "default") String sort,
            @RequestParam(required = false) Long mainCategoryId,
            @RequestParam(required = false) Long subCategoryId,
            @RequestParam(required = false) Long smallCategoryId) {
        return combinePackageService.getAdminPackageAndScheduleList(page, size, mainCategoryId, subCategoryId, smallCategoryId, sort);
    }
}
