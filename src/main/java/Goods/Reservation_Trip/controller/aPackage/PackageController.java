package Goods.Reservation_Trip.controller.aPackage;

import Goods.Reservation_Trip.dto.aPackage.req.PageSaveRequestDto;
import Goods.Reservation_Trip.dto.aPackage.res.AdminPackageListResponseDto;
import Goods.Reservation_Trip.dto.aPackage.res.PackageEditResponseDto;
import Goods.Reservation_Trip.dto.aPackage.res.PackageImageUrlDto;
import Goods.Reservation_Trip.entity.Package;
import Goods.Reservation_Trip.entity.PackageCategory;
import Goods.Reservation_Trip.entity.PackageOption;
import Goods.Reservation_Trip.service.aPackage.CombinePackageService;
import Goods.Reservation_Trip.service.aPackage.PackageCategoryService;
import Goods.Reservation_Trip.service.aPackage.PackageImageService;
import Goods.Reservation_Trip.service.aPackage.PackageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
@RequiredArgsConstructor
public class PackageController {

    private final PackageService packageService;

    private final PackageImageService packageImageService;

    private final CombinePackageService combinePackageService;

    private final PackageCategoryService packageCategoryService;


    @GetMapping("/admin/package/new")
    public String PackageNew() {
        return "package/admin/package-new";
    }

    @PostMapping("/admin/package/save")
    public String PackageSave(@ModelAttribute PageSaveRequestDto requestDto) {
        packageService.save(requestDto.getAPackage(), requestDto.getPackageOption(), requestDto.getPackageSchedule());
        return "redirect:/admin/package/list";
    }

    @GetMapping("/admin/package/list")
    public String adminPackageList(@RequestParam(defaultValue = "0") int page,
                                   @RequestParam(defaultValue = "20") int size,
                                   @RequestParam(defaultValue = "default") String sort,
                                   @RequestParam(required = false) Long mainCategoryId,
                                   @RequestParam(required = false) Long subCategoryId,
                                   @RequestParam(required = false) Long smallCategoryId,
                                   Model model) {
        Page<AdminPackageListResponseDto> adminPackageListDto = combinePackageService.getAdminPackageAndScheduleList(page, size, mainCategoryId, subCategoryId, smallCategoryId, sort);
        model.addAttribute("packageList", adminPackageListDto.getContent());
        model.addAttribute("page", adminPackageListDto);
        model.addAttribute("currentPage", adminPackageListDto.getNumber());
        model.addAttribute("size", size);
        model.addAttribute("sortSelect", sort);
        return "package/admin/package-list";
    }

    @GetMapping("/admin/package/search/index")
    public String adminPackageSearchList(@RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "20") int size,
                                         @RequestParam(value = "keyword", required = false, defaultValue = "") String name,
                                         Model model) {
        Page<AdminPackageListResponseDto> adminPackageListResponseDtoPage = combinePackageService.getAdminPackageSearchList(page, size, name);
        model.addAttribute("packageList", adminPackageListResponseDtoPage.getContent());
        model.addAttribute("page", adminPackageListResponseDtoPage);
        model.addAttribute("currentPage", adminPackageListResponseDtoPage.getNumber());
        model.addAttribute("size", size);
        model.addAttribute("keywordQuery", name);

        return "package/admin/package-search";
    }

    @GetMapping("/admin/package/edit/{id}")
    public String adminPackageEdit(@PathVariable Long id, Model model) {

        PackageEditResponseDto aPackage = packageService.getPackageEdit(id);
        PackageImageUrlDto packageImageUrlDto = packageImageService.getPackageImageDto(id);
        List<PackageCategory> mainCategories = packageCategoryService.getMainCategories();
        List<PackageCategory> subCategories = packageCategoryService.getSubCategories(2);
        List<PackageCategory> smallCategories = packageCategoryService.getSubCategories(3);

        model.addAttribute("package", aPackage);
        model.addAttribute("schedule", aPackage.getSchedules());
        model.addAttribute("option", aPackage.getOptions());
        model.addAttribute("imageUrl", packageImageUrlDto);
        model.addAttribute("mainCategory", mainCategories);
        model.addAttribute("subCategory", subCategories);
        model.addAttribute("smallCategory", smallCategories);
        return "package/admin/package-edit";
    }

    @PostMapping("/admin/package/edit/{id}")
    public String adminPackageUpdate(@ModelAttribute PageSaveRequestDto requestDto,
                                     @PathVariable Long id) {
        packageService.update(requestDto.getAPackage(), requestDto.getPackageOption(), requestDto.getPackageSchedule(), id);

        return "redirect:/admin/package/list";
    }
}
