package Goods.Reservation_Trip.controller.HanController;

import Goods.Reservation_Trip.repository.aPackage.PackageRepository;
import Goods.Reservation_Trip.service.HanService.HanPackageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class HanPackageApiController {

    private final PackageRepository packageRepository;
    private final HanPackageService hanPackageService;

//    @PostMapping("/reservation/select-date")
//    @ResponseBody
//    public ResponseEntity<String> handleDateSelection(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate selectedDate) {
//        System.out.println("LocalDate로 받은 날짜: " + selectedDate);
//
//        hanPackageService.
//
//
//
//
//
//        return ResponseEntity.ok("날짜 확인 완료: " + selectedDate.toString());
//    }





}
