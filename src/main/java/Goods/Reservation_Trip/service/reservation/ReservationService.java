package Goods.Reservation_Trip.service.reservation;

import Goods.Reservation_Trip.config.ImageManager;
import Goods.Reservation_Trip.dto.member.res.MemberResponseDto;
import Goods.Reservation_Trip.dto.reservation.req.MemberReservationSearchDto;
import Goods.Reservation_Trip.dto.reservation.req.NotificationMessage;
import Goods.Reservation_Trip.dto.reservation.res.ReservationDetailsResponseDto;
import Goods.Reservation_Trip.dto.reservation.res.ReservationResponseDto;
import Goods.Reservation_Trip.entity.*;
import Goods.Reservation_Trip.enums.ReservationState;
import Goods.Reservation_Trip.repository.ReservationRepository;
import Goods.Reservation_Trip.repository.aPackage.PackageOptionRepository;
import Goods.Reservation_Trip.util.Formatter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Transactional
@RequiredArgsConstructor
@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;

    private final ImageManager imageManager;

    private final SimpMessagingTemplate messagingTemplate;

    private final PackageOptionRepository packageOptionRepository;

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

        //검색 기준이 국가명일때
        if("countryName".equals(search)){
            ReservationState state = stateMap.get(reservationState);

            reservationPage = (state != null)
                    ? reservationRepository.findByMainCategoryNameWithoutSpacesAndReservationState(trimKeyword,pageable,state)
                    : reservationRepository.findByMainCategoryNameWithoutSpaces(trimKeyword,pageable);
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

    /**
     * 회원 주문 리스트
     */
    public List<ReservationDetailsResponseDto> getReservationList(Long memberId) {
        //멤버 아이디로 해당 최근10개의 주문 리스트 가져옴
        List<Reservation> reservationList = reservationRepository.findTop10ByMemberIdOrderByCreatedAtDesc(memberId);

        List<ReservationDetailsResponseDto> responseDtos = new ArrayList<>();


        for (Reservation reservation : reservationList) {

            String reservationMembers = getReservationMembers(reservation);

            ReservationDetailsResponseDto reservationDetailsResponseDto = ReservationDetailsResponseDto.builder()
                    .reservationPK(reservation.getId())
                    .code(reservation.getCode())
                    .createdAt(Formatter.getLocalDate(reservation.getCreatedAt()))
                    .packageName(reservation.getAPackage().getPackageName())
                    .startDate(reservation.getStartDate().toString())
                    .endDate(reservation.getEndDate().toString())
                    .reservationMembers(reservationMembers)
                    .totalPay(Formatter.changeBigDecimalFormat(reservation.getTotalPay()))
                    .reservationState(reservation.getReservationState().getName())
                    .packagePK(reservation.getAPackage().getId())
                    .mainImage(imageManager.createImageUrl(reservation.getAPackage().getMainImage().getImageFullName()))
                    .build();

            responseDtos.add(reservationDetailsResponseDto);
        }

        return responseDtos;
    }

    /**
     * 예약자 명단(ex: 성인2명 유아 1명)
     */
    public String getReservationMembers(Reservation reservation) {
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

        reservationMembers.append("성인 " + adultSum + "인");

        if (childSum != 0) {
            reservationMembers.append(", 어린이 " + childSum + "인");
        }

        if (infantSum != 0) {
            reservationMembers.append(", 유아 " + infantSum + "명");
        }

        return reservationMembers.toString();
    }

    public List<ReservationDetailsResponseDto> getReservationSearchList(Long memberId, MemberReservationSearchDto searchDto) {

        List<ReservationDetailsResponseDto> responseDtos = new ArrayList<>();

        //시작일을 연,월,일로 분리 후 int로 변환
        int startYear = Integer.parseInt(searchDto.getStartDate().substring(0, 4));
        int startMonth = Integer.parseInt(searchDto.getStartDate().substring(5, 7));
        int startDay = Integer.parseInt(searchDto.getStartDate().substring(8, 10));

        LocalDate startDate = LocalDate.of(startYear, startMonth, startDay);

        //종료일을 연,월,일로 분리 후 int로 변환
        int endYear = Integer.parseInt(searchDto.getEndDate().substring(0, 4));
        int endMonth = Integer.parseInt(searchDto.getEndDate().substring(5, 7));
        int endDay = Integer.parseInt(searchDto.getEndDate().substring(8, 10));

        LocalDate endDate = LocalDate.of(endYear, endMonth, endDay);

        //DB에 localDateTime 형식으로 저장되어있으므로 찾을 때도 localDateTime으로 바꿔줘야함
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(23, 59, 59);

        List<Reservation> reservationList = null;

        //예약일 기준으로 검색
        if (searchDto.getSearchSelect().equals("createdAt")) {
            reservationList = reservationRepository.findByMemberIdAndCreatedAtBetween(memberId, startDateTime, endDateTime);

            //시작일 기준으로 검색
        } else if (searchDto.getSearchSelect().equals("startedAt")) {
            reservationList = reservationRepository.findByMemberIdAndStartDateBetween(memberId, startDate, endDate);
        } else {
            throw new RuntimeException("잘못된 접근");
        }

        for (Reservation reservation : reservationList) {

            String reservationMembers = getReservationMembers(reservation);

            ReservationDetailsResponseDto reservationDetailsResponseDto = ReservationDetailsResponseDto.builder()
                    .reservationPK(reservation.getId())
                    .code(reservation.getCode())
                    .createdAt(Formatter.getLocalDate(reservation.getCreatedAt()))
                    .packageName(reservation.getAPackage().getPackageName())
                    .startDate(reservation.getStartDate().toString())
                    .endDate(reservation.getEndDate().toString())
                    .reservationMembers(reservationMembers)
                    .totalPay(Formatter.changeBigDecimalFormat(reservation.getTotalPay()))
                    .reservationState(reservation.getReservationState().getName())
                    .packagePK(reservation.getAPackage().getId())
                    .mainImage(imageManager.createImageUrl(reservation.getAPackage().getMainImage().getImageFullName()))
                    .build();

            responseDtos.add(reservationDetailsResponseDto);
        }

        return responseDtos;

    }

    /**
     * 예약 금액 상세
     */
    public List<String> getDetailReservationPay(Reservation reservation) {
        List<String> payList = new ArrayList<>();

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

        payList.add("성인 " + adultSum + "인 : " + Formatter.BigDecimalFormat(reservation.getAdultSumPrice()));

        if (childSum != 0) {
            payList.add("어린이 " + childSum + "인 : " + Formatter.BigDecimalFormat(reservation.getChildSumPrice()));
        }

        if (infantSum != 0) {
            payList.add("유아 " + infantSum + "인 : " + Formatter.BigDecimalFormat(reservation.getBabySumPrice()));
        }

        return payList;
    }

    /**
     * 관리자 예약 상세 페이지
     */
    public ReservationDetailsResponseDto getReservationDetails(Long id) {
        Optional<Reservation> reservationOptional = reservationRepository.findById(id);

        if (reservationOptional.isEmpty()) return null;

        Reservation reservation = reservationOptional.get();

        //연령대별 금액 리스트에 추가
        List<String> pricesByAgeGroup = new ArrayList<>();
        pricesByAgeGroup.add("성인 : " + Formatter.changeBigDecimalFormat(reservation.getAPackage().getAdultPrice()));
        pricesByAgeGroup.add("어린이 : " + Formatter.changeBigDecimalFormat(reservation.getAPackage().getChildPrice()));
        pricesByAgeGroup.add("유아 : " + Formatter.changeBigDecimalFormat(reservation.getAPackage().getBabyPrice()));

        //예약 명단 분류
        List<MemberResponseDto> memberResponseDtoList = new ArrayList<>();

        for (ReservationDetails reservationDetails : reservation.getReservationDetailsList()) {
            MemberResponseDto memberResponseDto = MemberResponseDto.builder()
                    .name(reservationDetails.getName())
                    .gender(reservationDetails.isGender())
                    .birth(Formatter.getBirth(reservationDetails.getBirth()))
                    .phoneNumber(Formatter.changePhoneNumber(reservationDetails.getPhoneNumber()))
                    .passportNumber(reservationDetails.getPassportNum())
                    .build();

            memberResponseDtoList.add(memberResponseDto);
        }


        PackageSchedule packageScheduleCheck = new PackageSchedule();

        //여행출발일과 여행일정 리스트중에 출발일이 같은것을 찾기
        for (PackageSchedule packageScheduleDto : reservation.getAPackage().getPackageScheduleList()) {
            if (reservation.getStartDate().isEqual(packageScheduleDto.getDepartureDateOut())) {
                packageScheduleCheck = packageScheduleDto;
            }
        }

        PackageScheduleDetails packageDetails = packageScheduleCheck.getPackageScheduleDetails();

        //패키지 옵션
//        Optional<PackageOption> optionalPackageOption = packageOptionRepository.findByAPackageId(reservation.getAPackage().getId());
//
//        if(optionalPackageOption.isEmpty()) throw new RuntimeException("패키지 옵션 없음");
//
//        PackageOption packageOption = optionalPackageOption.get();
//
//        List<String> optionList = Formatter.getPackageOptions(packageOption);

        return ReservationDetailsResponseDto.builder()
                .reservationPK(reservation.getId()) //예약 PK
                .code(reservation.getCode()) //예약번호
                .createdAt(Formatter.getLocalDate(reservation.getCreatedAt())) //예약일시
                .reservationState(reservation.getReservationState().getName()) //예약상태
                .name(reservation.getMember().getName()) //예약자 이름
                .email(reservation.getMember().getEmail()) //예약자 이메일
                .phoneNumber(Formatter.changePhoneNumber(reservation.getMember().getPhoneNumber())) //예약자 휴대전화번호
                .totalPay(Formatter.changeBigDecimalFormat(reservation.getTotalPay())) //총 결제금액
                .detailPay(getDetailReservationPay(reservation)) //상세 금액
                .aid(reservation.getAid()) //카카오페이 결제고유번호
                .payType(reservation.getPayType()) //결제 타입
                .approvedAt(Formatter.getLocalDate(reservation.getApprovedAt())) //카카오페이 결제승인시각
                .packagePK(reservation.getAPackage().getId()) //패키지 PK
                .packageName(reservation.getAPackage().getPackageName()) //패키지명
                .mainImage(imageManager.createImageUrl(reservation.getAPackage().getMainImage().getImageFullName())) //패키지 메인 이미지
                .pricesByAgeGroup(pricesByAgeGroup) //연령대별 패키지 금액
                .memberList(memberResponseDtoList) //예약 인원
                .country(reservation.getAPackage().getMainCategory().getName()) //여행하는 나라
                .fuelSurcharge(Formatter.BigDecimalFormat(reservation.getAPackage().getFuelSurcharge())) //유류할증료
                .hotel(reservation.getAPackage().getHotelName())
                .s_departureTime(packageScheduleCheck.getDepartureDateOut().toString() +" "+ packageDetails.getDepartureTimeOut()) // 여행 가는 날 출발 시간
                .s_origin(packageDetails.getDeparturePointOut().getName()) // 여행 가는 날 출발지
                .s_departureFlight(packageDetails.getFlightNumberOut()) //여행 가는 날 출발 항공편
                .s_arrivalTime(packageScheduleCheck.getArrivalDateOut().toString() + " "+ packageDetails.getArrivalTimeOut()) //여행 가는 날 도착 시간
                .s_destination(packageDetails.getArrivalPointOut().getName()) //여행 가는 날 도착지
                .e_departureTime(packageScheduleCheck.getDepartureDateReturn().toString() + " "+ packageDetails.getDepartureTimeReturn()) // 여행 종료일  출발 시간
                .e_origin(packageDetails.getDeparturePointReturn().getName()) // 여행 종료일 출발지
                .e_departureFlight(packageDetails.getFlightNumberReturn()) //여행 종료일 출발 항공편
                .e_arrivalTime(packageScheduleCheck.getArrivalDateReturn().toString() +" "+ packageDetails.getArrivalTimeReturn()) //여행 종료일 도착 시간
                .e_destination(packageDetails.getArrivalPointReturn().getName()) //여행 종료일 도착지
                .s_airlineName(packageDetails.getAirlineOut().getName()) //출국 항공사명
                .e_airlineName(packageDetails.getAirlineReturn().getName()) //귀국 항공사명
//                .option(optionList)
                .build();
    }

    /**
     * 예약 상태 변경
     */
    public String editReservationState(ReservationResponseDto reservationResponseDto) {
        Optional<Reservation> optionalReservation = reservationRepository.findById(reservationResponseDto.getReservationPK());

        if (optionalReservation.isEmpty()) return "500";

        Reservation reservation = optionalReservation.get();

        String state = reservationResponseDto.getReservationState();

        if (state.equals("CONFIRM")) {
            reservation.setReservationState(ReservationState.CONFIRM);
        } else if (state.equals("CANCEL")) {
            reservation.setReservationState(ReservationState.CANCEL);
        } else if (state.equals("WAIT")) {
            reservation.setReservationState(ReservationState.WAIT);
        } else if (state.equals("REQUEST")) {
            reservation.setReservationState(ReservationState.REQUEST);
        } else return "500";

        reservationRepository.save(reservation);

        return reservation.getReservationState().getName();
    }


    public void sendCancelNotification(String reservationCode,Long memberId){
        Optional<Reservation> optionalReservation = reservationRepository.findByMemberIdAndCode(memberId,reservationCode);

        if(optionalReservation.isEmpty()) throw new RuntimeException("잘못된 접근");

        Reservation reservation = optionalReservation.get();

        reservation.setReservationState(ReservationState.REQUEST);

        reservationRepository.save(reservation);

        NotificationMessage message = new NotificationMessage("예약번호 "+reservationCode+"의 예약취소 요청이 있습니다. ","cancel");
        messagingTemplate.convertAndSend("/topic/admin",message);
    }
}
