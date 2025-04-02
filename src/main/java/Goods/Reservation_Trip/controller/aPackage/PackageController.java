package Goods.Reservation_Trip.controller.aPackage;

import Goods.Reservation_Trip.dto.aPackage.res.AdminPackageListResponseDto;
import Goods.Reservation_Trip.dto.aPackage.req.PackageOptionRequestDto;
import Goods.Reservation_Trip.dto.aPackage.req.PackageRequestDto;
import Goods.Reservation_Trip.dto.aPackage.req.PackageScheduleListRequestDto;
import Goods.Reservation_Trip.service.aPackage.PackageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@Slf4j
public class PackageController {

    private final PackageService packageService;

    @GetMapping("/Q")
    public String Q() {
        return "package/admin/Q";
    }

    @GetMapping("/admin/package/new")
    public String PackageNew() {
        return "package/admin/package-new";
    }

    @PostMapping("/admin/package/save")
    public String PackageSave(
                              @ModelAttribute PackageRequestDto requestDto,
                              @ModelAttribute PackageOptionRequestDto optionRequestDto,
                              @ModelAttribute PackageScheduleListRequestDto scheduleRequestDtoList) {
        packageService.save(requestDto, optionRequestDto, scheduleRequestDtoList.getSchedules());
        return "redirect:/Q";
    }

    @GetMapping("/admin/package/list")
    public String adminPackageList(@RequestParam(defaultValue = "0") int page,
                                   @RequestParam(defaultValue = "10") int size,
                                   Model model) {
        Page<AdminPackageListResponseDto> adminPackageListDto = packageService.getAdminPackageListDto(page, size);
        model.addAttribute("packageList", adminPackageListDto.getContent());
        return "package/admin/package-list";
    }
}
