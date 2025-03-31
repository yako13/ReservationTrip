package Goods.Reservation_Trip.service;

import Goods.Reservation_Trip.dto.PackageRequestDto;
import Goods.Reservation_Trip.entity.Package;
import Goods.Reservation_Trip.entity.PackageSchedule;
import Goods.Reservation_Trip.repository.PackageScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PackageScheduleService {

    private final PackageScheduleRepository packageScheduleRepository;

    public PackageSchedule save(Package aPackage, PackageRequestDto requestDto) {
        PackageSchedule packageSchedule = PackageSchedule.builder()
                .departureDate(requestDto.getDepartureDate())
                .returnDate(requestDto.getReturnDate())
//                .maximumSeats(requestDto.getMaximumSeats())
//                .minimumSeats(requestDto.getMinimumSeats())
//                .seatsAvailable(requestDto)
                .departurePointOut(requestDto.getDeparturePointOut())
                .arrivalPointOut(requestDto.getArrivalPointOut())
                .departurePointReturn(requestDto.getDeparturePointReturn())
                .arrivalPointReturn(requestDto.getArrivalPointReturn())
                .period(requestDto.getPeriod())
                .airlineOut(requestDto.getAirlineOut())
                .airlineReturn(requestDto.getAirlineReturn())
                .flightNumberOut(requestDto.getFlightNumberOut())
                .flightNumberReturn(requestDto.getFlightNumberReturn())
                .packageStatus(requestDto.getPackageStatus())
                .departureTimeOut(requestDto.getDepartureTimeOut())
                .arrivalTimeOut(requestDto.getArrivalTimeOut())
                .departureTimeReturn(requestDto.getDepartureTimeReturn())
                .arrivalTimeReturn(requestDto.getArrivalTimeReturn())
                .build();

        return packageScheduleRepository.save(packageSchedule);
    }
}
