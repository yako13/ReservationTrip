package Goods.Reservation_Trip.controller.aPackage;

import Goods.Reservation_Trip.dto.aPackage.PackageRequestDto;
import Goods.Reservation_Trip.service.aPackage.PackageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class PackageController {

    private final PackageService packageService;

    @GetMapping("/Q")
    public String Q() {
        return "Q";
    }

    @GetMapping("/admin/package/new")
    public String PackageNew() {
        return "admin/package-new";
    }

    @PostMapping("/admin/package/save")
    public String PackageSave(@ModelAttribute PackageRequestDto requestDto) {
        packageService.save(requestDto);
        return "redirect:/Q";
    }

    @GetMapping("/admin/package/list")
    public String adminPackageList(){

        return "/admin/package-list";
    }
}
