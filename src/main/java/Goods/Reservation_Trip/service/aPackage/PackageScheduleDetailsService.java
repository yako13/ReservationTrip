package Goods.Reservation_Trip.service.aPackage;

import Goods.Reservation_Trip.dto.aPackage.req.PackageScheduleDetailsRequestDto;
import Goods.Reservation_Trip.entity.PackageSchedule;
import Goods.Reservation_Trip.entity.PackageScheduleDetails;
import Goods.Reservation_Trip.repository.aPackage.PackageScheduleDetailsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PackageScheduleDetailsService {

    private final PackageScheduleDetailsRepository packageScheduleDetailsRepository;

    public void save(PackageSchedule packageSchedule, PackageScheduleDetailsRequestDto requestDto){
        PackageScheduleDetails packageScheduleDetails = PackageScheduleDetails.builder()
                .packageSchedule(packageSchedule)
                .airlineOut(requestDto.getAirlineOut())
                .airlineReturn(requestDto.getAirlineReturn())
                .flightNumberOut(requestDto.getFlightNumberOut())
                .flightNumberReturn(requestDto.getFlightNumberReturn())
                .departurePointOut(requestDto.getDeparturePointOut())
                .arrivalPointOut(requestDto.getArrivalPointOut())
                .departureTimeOut(requestDto.getDepartureTimeOut())
                .arrivalTimeOut(requestDto.getArrivalTimeOut())
                .departurePointReturn(requestDto.getDeparturePointReturn())
                .arrivalPointReturn(requestDto.getArrivalPointReturn())
                .departureTimeReturn(requestDto.getDepartureTimeReturn())
                .arrivalTimeReturn(requestDto.getArrivalTimeReturn())
                .build();

        packageScheduleDetailsRepository.save(packageScheduleDetails);
    }
}
