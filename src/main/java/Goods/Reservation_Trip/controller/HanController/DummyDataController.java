package Goods.Reservation_Trip.controller.HanController;

import Goods.Reservation_Trip.service.HanService.DummyDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DummyDataController {

    private final DummyDataService dummyDataService;

    @PostMapping("/generate")
    public ResponseEntity<String> generateDummy(@RequestParam(defaultValue = "5") int count) {
        dummyDataService.generateDummyPackages(count);
        return ResponseEntity.ok(count + "개의 더미 패키지를 생성했습니다.");
    }
}
