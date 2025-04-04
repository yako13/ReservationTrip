package Goods.Reservation_Trip.controller.aPackage;

import Goods.Reservation_Trip.dto.aPackage.req.PageSaveRequestDto;
import Goods.Reservation_Trip.dto.aPackage.res.AdminPackageListResponseDto;
import Goods.Reservation_Trip.service.aPackage.CombinePackageService;
import Goods.Reservation_Trip.service.aPackage.PackageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class PackageController {

    private final PackageService packageService;

    private final CombinePackageService combinePackageService;


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
                                   Model model) {
        Page<AdminPackageListResponseDto> adminPackageListDto = combinePackageService.getAdminPackageAndScheduleList(page, size);
        model.addAttribute("packageList", adminPackageListDto.getContent());
        model.addAttribute("page", adminPackageListDto);
        model.addAttribute("currentPage", adminPackageListDto.getNumber());
        return "package/admin/package-list";
    }
}
