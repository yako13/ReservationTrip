package Goods.Reservation_Trip.service.aPackage;

import Goods.Reservation_Trip.dto.aPackage.req.PackageScheduleDetailsRequestDto;
import Goods.Reservation_Trip.dto.aPackage.req.PackageScheduleRequestDto;
import Goods.Reservation_Trip.entity.Package;
import Goods.Reservation_Trip.entity.PackageSchedule;
import Goods.Reservation_Trip.enums.PackageStatus;
import Goods.Reservation_Trip.repository.aPackage.PackageScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PackageScheduleService {

    private final PackageScheduleRepository packageScheduleRepository;

    private final PackageScheduleDetailsService packageScheduleDetailsService;


    public List<PackageSchedule> saveAll(Package aPackage, List<PackageScheduleRequestDto> requestDto) {
        if (requestDto == null) {
            throw new IllegalArgumentException("스케줄 요청 데이터가 null입니다.");
        }

        List<PackageSchedule> scheduleList = new ArrayList<>();

        for (PackageScheduleRequestDto scheduleRequestDto : requestDto) {
                PackageSchedule packageSchedule = PackageSchedule.builder()
                        .aPackage(aPackage)
                        .maximumMember(scheduleRequestDto.getMaximumMember())
                        .minimumRequired(scheduleRequestDto.getMinimumRequired())
                        .departureDateOut(scheduleRequestDto.getDepartureDateOut())
                        .arrivalDateOut(scheduleRequestDto.getArrivalDateOut())
                        .departureDateReturn(scheduleRequestDto.getDepartureDateReturn())
                        .arrivalDateReturn(scheduleRequestDto.getArrivalDateReturn())
                        .packageStatus(PackageStatus.AVAILABLE)
                        .build();

            scheduleList.add(packageSchedule);
        }

        List<PackageSchedule> savedScheduleList = packageScheduleRepository.saveAll(scheduleList);

        for (int i = 0; i < savedScheduleList.size(); i++) {

            PackageScheduleDetailsRequestDto details = requestDto.get(i).getDetails();

            if (details != null) {
            packageScheduleDetailsService.save(savedScheduleList.get(i), requestDto.get(i).getDetails());
            } else {
                System.out.println("details 의 값이 NULL");
            }
        }
        return savedScheduleList;
    }
}
