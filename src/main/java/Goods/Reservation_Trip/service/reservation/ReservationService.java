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

import java.util.Map;

@Transactional
@RequiredArgsConstructor
@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;

    public Page<ReservationResponseDto> pageReservation(int page, int size, String sort, String reservationState) {

        //페이지당 예약 수 50개이상 불러올 때
        if (size >= 50) {
            throw new RuntimeException("올바르지 않은 접근");
        }

        Pageable pageable;

        //가격으로 sort
        if ("price_asc".equals(sort)) {
            pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "totalPay"));
        } else if ("price_desc".equals(sort)) {
            pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "totalPay"));

            //주문 날짜별 sort
        } else if ("past".equals(sort)) {
            pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "createdAt"));
        } else {
            pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        }

        Page<Reservation> reservationPage = null;


        //주문 상태 sort
        if (reservationState.equals("CONFIRM")) {
            reservationPage = reservationRepository.findByReservationState(ReservationState.CONFIRM, pageable);
        } else if (reservationState.equals("CANCEL")) {
            reservationPage = reservationRepository.findByReservationState(ReservationState.CANCEL, pageable);
        } else if (reservationState.equals("WAIT")) {
            reservationPage = reservationRepository.findByReservationState(ReservationState.WAIT, pageable);
        } else if (reservationState.equals("REQUEST")) {
            reservationPage = reservationRepository.findByReservationState(ReservationState.REQUEST, pageable);
        } else {
            reservationPage = reservationRepository.findAll(pageable);
        }

        return reservationPage.map(reservation ->
                ReservationResponseDto.builder()
                        .reservationPK(reservation.getId())
                        .packageName(reservation.getAPackage().getPackageName())
                        .memberName(reservation.getMember().getName())
                        .reservationCode(reservation.getCode())
                        .totalPay(Formatter.changeBigDecimalFormat(reservation.getTotalPay()))
                        .reservationState(reservation.getReservationState().getName())
                        .createdAt(Formatter.getLocalDate(reservation.getCreatedAt()))
                        .modifiedAt(Formatter.getLocalDate(reservation.getModifiedAt()))
                        .build()
        );

    }

    public Page<ReservationResponseDto> pageReservationSearch(String keyword, String search, int page, int size, String sort, String reservationState) {

        //페이지당 예약 수 50개이상 불러올 때
        if (size >= 50) {
            throw new RuntimeException("올바르지 않은 접근");
        }

        Pageable pageable;

        //가격으로 sort
        if ("price_asc".equals(sort)) {
            pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "totalPay"));
        } else if ("price_desc".equals(sort)) {
            pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "totalPay"));

            //주문 날짜별 sort
        } else if ("past".equals(sort)) {
            pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "createdAt"));
        } else {
            pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        }

        if (keyword == null || keyword.isBlank()) return Page.empty();

        String trimKeyword = keyword.trim();

        Page<Reservation> reservationPage = null;

        Map<String, ReservationState> stateMap = Map.of(
                "CONFIRM", ReservationState.CONFIRM,
                "WAIT", ReservationState.WAIT,
                "CANCEL", ReservationState.CANCEL,
                "REQUEST", ReservationState.REQUEST
        );

        //검색기준이 예약번호일때
        if("reservationCode".equals(search)){
            ReservationState state = stateMap.get(reservationState);

            reservationPage = (state != null)
                    ? reservationRepository.findByCodeContainingWithoutSpaceAndReservationState(trimKeyword, pageable, state)
                    : reservationRepository.findByCodeContainingWithoutSpace(trimKeyword, pageable);
        }



        //검색 기준이 예약자일 때
        if ("ordererName".equals(search)) {

            ReservationState state = stateMap.get(reservationState);

            reservationPage = (state != null)
                    ? reservationRepository.findByMemberNameContainingWithoutSpaceAndReservationState(trimKeyword, pageable, state)
                    : reservationRepository.findByMemberNameContainingWithoutSpace(trimKeyword, pageable);
        }

        //검색 기준이 패키지명일 때
        if ("packageName".equals(search)) {

            ReservationState state = stateMap.get(reservationState);

            reservationPage = (state != null)
                    ? reservationRepository.findByAPackagePackageNameContainingWithoutSpaceAndReservationState(trimKeyword, pageable, state)
                    : reservationRepository.findByAPackagePackageNameContainingWithoutSpace(trimKeyword, pageable);
        }


        return reservationPage.map(reservation ->
                ReservationResponseDto.builder()
                        .reservationPK(reservation.getId())
                        .packageName(reservation.getAPackage().getPackageName())
                        .memberName(reservation.getMember().getName())
                        .reservationCode(reservation.getCode())
                        .totalPay(Formatter.changeBigDecimalFormat(reservation.getTotalPay()))
                        .reservationState(reservation.getReservationState().getName())
                        .createdAt(Formatter.getLocalDate(reservation.getCreatedAt()))
                        .modifiedAt(Formatter.getLocalDate(reservation.getModifiedAt()))
                        .build()
        );

    }
}
