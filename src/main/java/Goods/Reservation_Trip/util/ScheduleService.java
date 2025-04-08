package Goods.Reservation_Trip.util;

import Goods.Reservation_Trip.repository.aPackage.PackageScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Async
@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final PackageScheduleRepository packageScheduleRepository;

    // 초 분 시 일 월 요일 지정해서 명령 수행 (서버가 켜져있을때만 적용)
    @Transactional
    @Scheduled(cron = "0 59 23 * * *")
    public void changeStatus(){
        int updatedRows = packageScheduleRepository.updateExpiredSchedules();
        if (updatedRows > 0) {
            System.out.println("매일 23시 59분 00초에 패키지 상태를 변경합니다." + updatedRows + "개의 데이터가 교체됨");
        }
        System.out.println("매일 23시 59분 00초에 패키지 상태를 변경합니다. 교체된 데이터 없음");
    }

    // 서버 시작시 한번만 실행 됨
    @Transactional
    @EventListener(ApplicationReadyEvent.class)
    public void checkStatus() {
        int updatedRows = packageScheduleRepository.updateExpiredSchedules();
        if (updatedRows > 0) {
            System.out.println("서버 시작시 한번만 패키지 상태를 변경합니다." + updatedRows + "개의 데이터가 교체됨");
        }
        System.out.println("서버 시작시 한번만 패키지 상태를 변경합니다. 교체된 데이터 없음");
    }
}
