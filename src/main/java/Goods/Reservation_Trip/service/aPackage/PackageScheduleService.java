package Goods.Reservation_Trip.service.aPackage;

import Goods.Reservation_Trip.dto.aPackage.req.AirlineDto;
import Goods.Reservation_Trip.dto.aPackage.req.AirportDto;
import Goods.Reservation_Trip.dto.aPackage.req.PackageScheduleDetailsRequestDto;
import Goods.Reservation_Trip.dto.aPackage.req.PackageScheduleRequestDto;
import Goods.Reservation_Trip.dto.aPackage.res.PackageOptionEditResponseDto;
import Goods.Reservation_Trip.dto.aPackage.res.PackageScheduleDetailsEditResponseDto;
import Goods.Reservation_Trip.dto.aPackage.res.PackageScheduleEditResponseDto;
import Goods.Reservation_Trip.entity.Package;
import Goods.Reservation_Trip.entity.PackageSchedule;
import Goods.Reservation_Trip.enums.PackageStatus;
import Goods.Reservation_Trip.repository.aPackage.PackageScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PackageScheduleService {

    private final PackageScheduleRepository packageScheduleRepository;

    private final PackageScheduleDetailsService packageScheduleDetailsService;


    @Transactional
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
                    .reservedMemberCount(0)
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

    public List<PackageScheduleEditResponseDto> getPackageScheduleEdit(Long id) {
        List<PackageSchedule> scheduleList = packageScheduleRepository.findByaPackage_Id(id);

        if (scheduleList.isEmpty()) throw new RuntimeException("해당 상품의 일정 없음");

        return scheduleList.stream()
                .map(packageSchedule -> PackageScheduleEditResponseDto.builder()
                        .id(packageSchedule.getId())
                        .maximumMember(packageSchedule.getMaximumMember())
                        .minimumRequired(packageSchedule.getMinimumRequired())
                        .reservedMemberCount(packageSchedule.getReservedMemberCount())
                        .packageStatus(packageSchedule.getPackageStatus())
                        .departureDateOut(packageSchedule.getDepartureDateOut())
                        .arrivalDateOut(packageSchedule.getArrivalDateOut())
                        .departureDateReturn(packageSchedule.getDepartureDateReturn())
                        .arrivalDateReturn(packageSchedule.getArrivalDateReturn())
                        .details(PackageScheduleDetailsEditResponseDto.builder()
                                .id(packageSchedule.getPackageScheduleDetails().getId())
                                .airlineOutId(AirlineDto.builder()
                                        .id(packageSchedule.getPackageScheduleDetails().getAirlineOut().getId())
                                        .name(packageSchedule.getPackageScheduleDetails().getAirlineOut().getName())
                                        .build())
                                .airlineReturnId(AirlineDto.builder()
                                        .id(packageSchedule.getPackageScheduleDetails().getId())
                                        .name(packageSchedule.getPackageScheduleDetails().getAirlineReturn().getName())
                                        .build())
                                .flightNumberOut(packageSchedule.getPackageScheduleDetails().getFlightNumberOut())
                                .flightNumberReturn(packageSchedule.getPackageScheduleDetails().getFlightNumberReturn())
                                .departurePointOutId(AirportDto.builder()
                                        .id(packageSchedule.getPackageScheduleDetails().getId())
                                        .name(packageSchedule.getPackageScheduleDetails().getDeparturePointOut().getName())
                                        .code(packageSchedule.getPackageScheduleDetails().getDeparturePointOut().getCode())
                                        .build())
                                .arrivalPointOutId(AirportDto.builder()
                                        .id(packageSchedule.getPackageScheduleDetails().getId())
                                        .name(packageSchedule.getPackageScheduleDetails().getArrivalPointOut().getName())
                                        .code(packageSchedule.getPackageScheduleDetails().getArrivalPointOut().getCode())
                                        .build())
                                .departurePointReturnId(AirportDto.builder()
                                        .id(packageSchedule.getPackageScheduleDetails().getDeparturePointReturn().getId())
                                        .name(packageSchedule.getPackageScheduleDetails().getDeparturePointReturn().getName())
                                        .code(packageSchedule.getPackageScheduleDetails().getDeparturePointReturn().getCode())
                                        .build())
                                .arrivalPointReturnId(AirportDto.builder()
                                        .id(packageSchedule.getPackageScheduleDetails().getArrivalPointReturn().getId())
                                        .name(packageSchedule.getPackageScheduleDetails().getArrivalPointReturn().getName())
                                        .code(packageSchedule.getPackageScheduleDetails().getArrivalPointReturn().getCode())
                                        .build())
                                .departureTimeOut(packageSchedule.getPackageScheduleDetails().getDepartureTimeOut())
                                .arrivalTimeOut(packageSchedule.getPackageScheduleDetails().getArrivalTimeOut())
                                .departureTimeReturn(packageSchedule.getPackageScheduleDetails().getDepartureTimeReturn())
                                .arrivalTimeReturn(packageSchedule.getPackageScheduleDetails().getArrivalTimeReturn())
                                .build())
                        .build()
                )
                .collect(Collectors.toList());
    }
}
