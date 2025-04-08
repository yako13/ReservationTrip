package Goods.Reservation_Trip.service.aPackage;

import Goods.Reservation_Trip.dto.aPackage.req.PackageScheduleDetailsRequestDto;
import Goods.Reservation_Trip.entity.Airline;
import Goods.Reservation_Trip.entity.Airport;
import Goods.Reservation_Trip.entity.PackageSchedule;
import Goods.Reservation_Trip.entity.PackageScheduleDetails;
import Goods.Reservation_Trip.repository.aPackage.AirlineRepository;
import Goods.Reservation_Trip.repository.aPackage.AirportRepository;
import Goods.Reservation_Trip.repository.aPackage.PackageScheduleDetailsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PackageScheduleDetailsService {

    private final PackageScheduleDetailsRepository packageScheduleDetailsRepository;

    private final AirlineRepository airlineRepository;

    private final AirportRepository airportRepository;

    public void save(PackageSchedule packageSchedule, PackageScheduleDetailsRequestDto requestDto){

        Airline airlineOut = airlineRepository.findById(requestDto.getAirlineOutId())
                .orElseThrow(() -> new IllegalArgumentException("출국 항공사 없음"));
        Airline airlineReturn = airlineRepository.findById(requestDto.getAirlineReturnId())
                .orElseThrow(() -> new IllegalArgumentException("귀국 항공사 없음"));
        Airport departurePointOut = airportRepository.findById(requestDto.getDeparturePointOutId())
                .orElseThrow(() -> new IllegalArgumentException("출국 출발지 공항 없음"));
        Airport arrivalPointOut = airportRepository.findById(requestDto.getArrivalPointOutId())
                .orElseThrow(() -> new IllegalArgumentException("출국 도착지 공항 없음"));
        Airport departurePointReturn = airportRepository.findById(requestDto.getDeparturePointReturnId())
                .orElseThrow(() -> new IllegalArgumentException("귀국 출발지 공항 없음"));
        Airport arrivalPointReturn = airportRepository.findById(requestDto.getArrivalPointReturnId())
                .orElseThrow(() -> new IllegalArgumentException("귀국 도착지 공항 없음"));

        PackageScheduleDetails packageScheduleDetails = PackageScheduleDetails.builder()
                .packageSchedule(packageSchedule)
                .airlineOut(airlineOut)
                .airlineReturn(airlineReturn)
                .flightNumberOut(requestDto.getFlightNumberOut())
                .flightNumberReturn(requestDto.getFlightNumberReturn())
                .departurePointOut(departurePointOut)
                .arrivalPointOut(arrivalPointOut)
                .departureTimeOut(requestDto.getDepartureTimeOut())
                .arrivalTimeOut(requestDto.getArrivalTimeOut())
                .departurePointReturn(departurePointReturn)
                .arrivalPointReturn(arrivalPointReturn)
                .departureTimeReturn(requestDto.getDepartureTimeReturn())
                .arrivalTimeReturn(requestDto.getArrivalTimeReturn())
                .build();

        packageScheduleDetailsRepository.save(packageScheduleDetails);
    }
}
