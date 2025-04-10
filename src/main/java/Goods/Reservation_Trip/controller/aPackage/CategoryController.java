package Goods.Reservation_Trip.controller.aPackage;

import Goods.Reservation_Trip.dto.aPackage.req.PackageCategoryDto;
import Goods.Reservation_Trip.dto.aPackage.res.PackageCategoryResponseDto;
import Goods.Reservation_Trip.service.aPackage.PackageCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class CategoryController {

    private final PackageCategoryService packageCategoryService;

    @GetMapping("/admin/package/category/new")
    public String adminPackageCategoryNewPage(Model model) {
        List<PackageCategoryResponseDto> packageCategoryResponseDtoList = packageCategoryService.getCategories();
        model.addAttribute("categoryList", packageCategoryResponseDtoList);
        return "package/admin/package-category-new";
    }

    @PostMapping("/admin/category/new")
    @ResponseBody
    public int adminRegisterCategory(PackageCategoryDto packageCategoryDto){
        return packageCategoryService.registerCategory(packageCategoryDto);
    }
}
