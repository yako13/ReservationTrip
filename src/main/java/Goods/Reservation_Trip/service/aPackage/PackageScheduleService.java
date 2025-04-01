package Goods.Reservation_Trip.service.aPackage;

import Goods.Reservation_Trip.dto.aPackage.PackageRequestDto;
import Goods.Reservation_Trip.entity.Package;
import Goods.Reservation_Trip.entity.PackageSchedule;
import Goods.Reservation_Trip.repository.aPackage.PackageScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class PackageScheduleService {

    private final PackageScheduleRepository packageScheduleRepository;

    public PackageSchedule save(Package aPackage, PackageRequestDto requestDto) {
        PackageSchedule packageSchedule = PackageSchedule.builder()
                .aPackage(aPackage)
                .departureDateOut(LocalDate.parse(requestDto.getDepartureDateOut()))
                .arrivalDateOut(LocalDate.parse(requestDto.getArrivalDateOut()))
                .departureDateReturn(LocalDate.parse(requestDto.getDepartureDateReturn()))
                .arrivalDateReturn(LocalDate.parse(requestDto.getArrivalDateReturn()))
                .departurePointOut(requestDto.getDeparturePointOut())
                .arrivalPointOut(requestDto.getArrivalPointOut())
                .departurePointReturn(requestDto.getDeparturePointReturn())
                .arrivalPointReturn(requestDto.getArrivalPointReturn())
                .period(requestDto.getPeriod())
                .airlineOut(requestDto.getAirlineOut())
                .airlineReturn(requestDto.getAirlineReturn())
                .flightNumberOut(requestDto.getFlightNumberOut())
                .flightNumberReturn(requestDto.getFlightNumberReturn())
                .departureTimeOut(requestDto.getDepartureTimeOut())
                .arrivalTimeOut(requestDto.getArrivalTimeOut())
                .departureTimeReturn(requestDto.getDepartureTimeReturn())
                .arrivalTimeReturn(requestDto.getArrivalTimeReturn())
                .build();

        return packageScheduleRepository.save(packageSchedule);
    }
}
