package Goods.Reservation_Trip.controller.aPackage;

import Goods.Reservation_Trip.dto.aPackage.req.AirportDto;
import Goods.Reservation_Trip.dto.aPackage.req.PackageAirportDto;
import Goods.Reservation_Trip.dto.aPackage.res.PackageAirportResponseDto;
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
public class AirportController {

    private final PackageCategoryAndAirportService packageCategoryAndAirportService;

    /**
     * 공항 등록 페이지
     */
    @GetMapping("/admin/package/airport/new")
    public String adminPackageAirportNewPage(Model model) {
        List<PackageCategoryResponseDto> packageCategoryResponseDtoList = packageCategoryAndAirportService.getPackageCategoryResponseDtos();

        model.addAttribute("categoryList", packageCategoryResponseDtoList);

        return "package/admin/package-airport-new";
    }

    /**
     * 공항 등록
     */
    @PostMapping("/admin/package/airport/new")
    @ResponseBody
    public int registerAirport(PackageAirportDto airportDto) {
        return packageCategoryAndAirportService.registerAirport(airportDto);
    }

    /**
     * 공항 삭제
     */
    @PostMapping("/admin/package/airport/delete")
    @ResponseBody
    public int deleteAirport(PackageAirportDto airportDto) {
//        return packageCategoryAndAirportService.deleteAirport(airportDto);
        return 100;
    }
}
