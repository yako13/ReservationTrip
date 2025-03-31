package Goods.Reservation_Trip.controller;

import Goods.Reservation_Trip.dto.PackageRequestDto;
import Goods.Reservation_Trip.enums.Airline;
import Goods.Reservation_Trip.service.PackageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

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
    public String PackageSave(PackageRequestDto requestDto) {
        packageService.save(requestDto);
        return "redirect:/Q";
    }
}
