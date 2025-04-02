package Goods.Reservation_Trip.service.aPackage;

import Goods.Reservation_Trip.dto.aPackage.req.PackageScheduleRequestDto;
import Goods.Reservation_Trip.dto.aPackage.res.AdminPackageListResponseDto;
import Goods.Reservation_Trip.entity.Package;
import Goods.Reservation_Trip.entity.PackageSchedule;
import Goods.Reservation_Trip.enums.PackageStatus;
import Goods.Reservation_Trip.repository.aPackage.PackageScheduleRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PackageScheduleService {

    private final PackageScheduleRepository packageScheduleRepository;

    private final ObjectMapper objectMapper;

    public List<PackageSchedule> saveAll(Package aPackage, List<PackageScheduleRequestDto> requestDto) {
        if (requestDto == null) {
            throw new IllegalArgumentException("스케줄 요청 데이터가 null입니다.");
        }

        List<PackageSchedule> schedules = requestDto.stream()
                .map(dto -> PackageSchedule.builder()
                        .aPackage(aPackage)
                        .departureDateOut(dto.getDepartureDateOut())
                        .arrivalDateOut(dto.getArrivalDateOut())
                        .departureDateReturn(dto.getDepartureDateReturn())
                        .arrivalDateReturn(dto.getArrivalDateReturn())
                        .departurePointOut(dto.getDeparturePointOut())
                        .arrivalPointOut(dto.getArrivalPointOut())
                        .departurePointReturn(dto.getDeparturePointReturn())
                        .arrivalPointReturn(dto.getArrivalPointReturn())
                        .period(dto.getPeriod())
                        .airlineOut(dto.getAirlineOut())
                        .airlineReturn(dto.getAirlineReturn())
                        .flightNumberOut(dto.getFlightNumberOut())
                        .flightNumberReturn(dto.getFlightNumberReturn())
                        .departureTimeOut(dto.getDepartureTimeOut())
                        .arrivalTimeOut(dto.getArrivalTimeOut())
                        .departureTimeReturn(dto.getDepartureTimeReturn())
                        .arrivalTimeReturn(dto.getArrivalTimeReturn())
                        .packageStatus(PackageStatus.AVAILABLE)
                        .build())
                .collect(Collectors.toList());

        return packageScheduleRepository.saveAll(schedules);
    }

    public Page<AdminPackageListResponseDto> getAdminPackageScheduleListDto(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return packageScheduleRepository.findAll(pageable).map(packageSchedule -> {
            return AdminPackageListResponseDto.builder()
                    .id(packageSchedule.getId())
                    .departurePointOut(String.valueOf(packageSchedule.getDeparturePointOut()))
                    .arrivalPointOut(String.valueOf(packageSchedule.getArrivalPointOut()))
                    .period(packageSchedule.getPeriod())
                    .name(packageSchedule.getAPackage().getPackageName())
                    .fuelSurchargeIncluded(packageSchedule.getAPackage().getFuelSurchargeIncluded())
                    .build();
        });
    }
}
