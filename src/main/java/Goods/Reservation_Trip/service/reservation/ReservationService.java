package Goods.Reservation_Trip.service.reservation;

import Goods.Reservation_Trip.dto.reservation.res.ReservationResponseDto;
import Goods.Reservation_Trip.entity.Reservation;
import Goods.Reservation_Trip.enums.ReservationState;
import Goods.Reservation_Trip.repository.ReservationRepository;
import Goods.Reservation_Trip.util.Formatter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;

    public Page<ReservationResponseDto> pageReservation(Pageable pageable){

        //페이지당 예약 수 50개이상 불러올 때
//        if(size>=50){
//            throw  new RuntimeException("올바르지 않은 접근");
//        }
//
//
//        //가격으로 sort
//        if ("price_asc".equals(sort)) {
//            pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "totalPay"));
//        } else if ("price_desc".equals(sort)) {
//            pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "totalPay"));
//
//            //주문 날짜별 sort
//        } else if ("past".equals(sort)) {
//            pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "createdAt"));
//        } else {
//            pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
//        }

        //주문 상태 sort
        Page<Reservation> reservationPage = reservationRepository.findAll(pageable);
//        if (reservationState.equals("CONFIRM")) {
//            reservationPage = reservationRepository.findByReservationState(ReservationState.CONFIRM, pageable);
//        } else if (reservationState.equals("CANCEL")) {
//            reservationPage = reservationRepository.findByReservationState(ReservationState.CANCEL, pageable);
//        } else if (reservationState.equals("WAIT")) {
//            reservationPage = reservationRepository.findByReservationState(ReservationState.WAIT, pageable);
//        }
//        else if(reservationState.equals("REQUEST")){
//            reservationPage = reservationRepository.findByReservationState(ReservationState.REQUEST, pageable);
//        }
//        else {
//            reservationPage = reservationRepository.findAll(pageable);
//        }

        return reservationPage.map(reservation ->
                    ReservationResponseDto.builder()
                            .reservationPK(reservation.getId())
                            .packageName(reservation.getAPackage().getPackageName())
                            .memberName(reservation.getMember().getName())
                            .reservationCode(Formatter.getReservationCode(reservation.getCreatedAt()))
                            .totalPay(Formatter.changeBigDecimalFormat(reservation.getTotalPay()))
                            .reservationState(reservation.getReservationState().getName())
                            .createdAt(Formatter.getLocalDate(reservation.getCreatedAt()))
                            .modifiedAt(Formatter.getLocalDate(reservation.getModifiedAt()))
                            .build()
                );

    }
}
