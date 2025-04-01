package Goods.Reservation_Trip.service.reservation;

import Goods.Reservation_Trip.config.ImageManager;
import Goods.Reservation_Trip.dto.reservation.res.ReservationDetailsResponseDto;
import Goods.Reservation_Trip.dto.reservation.res.ReservationResponseDto;
import Goods.Reservation_Trip.entity.Reservation;
import Goods.Reservation_Trip.entity.ReservationDetails;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Transactional
@RequiredArgsConstructor
@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;

    private final ImageManager imageManager;

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
        if ("reservationCode".equals(search)) {
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

    public List<ReservationDetailsResponseDto> getReservationDetails(Long memberId,Integer year,Integer month) {
        //멤버 아이디로 해당 주문 리스트 가져옴
        List<Reservation> reservationList = reservationRepository.findByMemberId(memberId);

        List<ReservationDetailsResponseDto> responseDtos = new ArrayList<>();


        for (Reservation reservation : reservationList) {

            StringBuilder reservationMembers = new StringBuilder();

            int adultSum = 0;
            int childSum = 0;
            int infantSum = 0;

            //예약자 연령대 계산
            for (ReservationDetails reservationDetails : reservation.getReservationDetailsList()) {
                if (reservationDetails.getAgeGroup().getName().equals("어린이")) {
                    childSum++;
                } else if (reservationDetails.getAgeGroup().getName().equals("성인")) {
                    adultSum++;
                } else if (reservationDetails.getAgeGroup().getName().equals("유아")) {
                    infantSum++;
                } else {
                    throw new RuntimeException("잘못된 접근");
                }
            }

            reservationMembers.append("성인 "+adultSum + "명");

            if (childSum != 0) {
                reservationMembers.append("어린이 "+childSum + "명");
            }

            if (infantSum != 0) {
                reservationMembers.append("유아 "+infantSum + "명");
            }

            ReservationDetailsResponseDto reservationDetailsResponseDto = ReservationDetailsResponseDto.builder()
                    .reservationPK(reservation.getId())
                    .code(reservation.getCode())
                    .createdAt(Formatter.getLocalDate(reservation.getCreatedAt()))
                    .packageName(reservation.getAPackage().getPackageName())
                    .startDate(reservation.getStartDate().toString())
                    .endDate(reservation.getEndDate().toString())
                    .reservationMembers(reservationMembers.toString())
                    .totalPay(Formatter.changeBigDecimalFormat(reservation.getTotalPay()))
                    .reservationState(reservation.getReservationState().getName())
                    .packagePK(reservation.getAPackage().getId())
                    .mainImage(imageManager.createImageUrl(reservation.getAPackage().getMainImage().getImageFullName()))
                    .build();

            responseDtos.add(reservationDetailsResponseDto);
        }

        return responseDtos;
    }
}
