package Goods.Reservation_Trip.controller.HanController;

import Goods.Reservation_Trip.dto.HanDto.PackApiDto;
import Goods.Reservation_Trip.repository.aPackage.PackageRepository;
import Goods.Reservation_Trip.service.HanService.HanPackageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@Slf4j
@RestController
@RequiredArgsConstructor
public class HanPackageApiController {

    private final PackageRepository packageRepository;
    private final HanPackageService hanPackageService;

    @PostMapping("/package/select-date")
    @ResponseBody
    public ResponseEntity<PackApiDto> handleDateSelection(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate selectedDate,
                                                      @RequestParam Long packagePk) {
        System.out.println("LocalDate로 받은 날짜: " + selectedDate);

        PackApiDto packApiDto = hanPackageService.PackageDateCheck(selectedDate,packagePk);

        if(packApiDto ==null){

            log.error("서비스에서 문제 발생");
        }


        return ResponseEntity.ok(packApiDto);
    }





}
