package Goods.Reservation_Trip.controller.aPackage;

import Goods.Reservation_Trip.dto.aPackage.req.PackageCategoryDto;
import Goods.Reservation_Trip.dto.aPackage.res.PackageCategoryResponseDto;
import Goods.Reservation_Trip.service.aPackage.PackageCategoryAndAirportService;
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

    private final PackageCategoryAndAirportService packageCategoryAndAirportService;

    /**
     * 카테고리 생성 페이지
     */
    @GetMapping("/admin/package/category/new")
    public String adminPackageCategoryNewPage(Model model) {
        List<PackageCategoryResponseDto> packageCategoryResponseDtoList = packageCategoryAndAirportService.getPackageCategoryResponseDtos();
        model.addAttribute("categoryList", packageCategoryResponseDtoList);
        return "package/admin/package-category-new";
    }

    /**
     *  카테고리 생성
     */
    @PostMapping("/admin/package/category/new")
    @ResponseBody
    public int adminRegisterCategory(PackageCategoryDto packageCategoryDto){
        return packageCategoryAndAirportService.registerCategory(packageCategoryDto);
    }

    /**
     * 카테고리 삭제
     */
    @PostMapping("/admin/package/category/delete")
    @ResponseBody
    public int adminDeleteCategory(PackageCategoryDto packageCategoryDto){
        return packageCategoryAndAirportService.deleteCategory(packageCategoryDto);
    }

    /**
     * 카테고리 수정 페이지
     */
    @GetMapping("/admin/package/category/edit")
    public String adminPackageCategoryEditPage(Model model) {
        List<PackageCategoryResponseDto> packageCategoryResponseDtoList = packageCategoryAndAirportService.getPackageCategoryResponseDtos();
        model.addAttribute("categoryList", packageCategoryResponseDtoList);
        return "package/admin/package-category-edit";
    }

    /**
     * 카테고리 이름 수정
     */
    @PostMapping("/admin/package/category/edit/name")
    @ResponseBody
    public int adminPackageCategoryEditName(PackageCategoryDto packageCategoryDto) {
        return packageCategoryAndAirportService.editCategory(packageCategoryDto);
    }

    /**
     * 카테고리 이동
     */
    @PostMapping("/admin/package/category/edit/move")
    @ResponseBody
    public int adminPackageCategoryEditMove(PackageCategoryDto packageCategoryDto) {
        return packageCategoryAndAirportService.editCategoryLocation(packageCategoryDto);
    }

}
