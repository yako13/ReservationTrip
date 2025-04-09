package Goods.Reservation_Trip.service.HanService;

import Goods.Reservation_Trip.dto.HanDto.*;
import Goods.Reservation_Trip.dto.member.res.MemberResponseDto;
import Goods.Reservation_Trip.entity.Package;
import Goods.Reservation_Trip.entity.*;
import Goods.Reservation_Trip.enums.ReservationState;
import Goods.Reservation_Trip.repository.HanPart.HanReservationDetailsRepository;
import Goods.Reservation_Trip.repository.HanPart.HanReservationRepository;
import Goods.Reservation_Trip.repository.MemberRepository;
import Goods.Reservation_Trip.repository.aPackage.PackageRepository;
import Goods.Reservation_Trip.service.member.MemberService;
import Goods.Reservation_Trip.util.Formatter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static Goods.Reservation_Trip.enums.ReservationState.REQUEST;

@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class HanReservationService {

    private final MemberRepository memberRepository;

    private final PackageRepository packageRepository;


    private final MemberService memberService;
    private final HanReservationRepository hanReservationRepository;
    private final HanReservationDetailsRepository hanReservationDetailsRepository;

    //예약 페이지로 가는 서비스
    public ResvPageDto ReservationPage(HttpServletRequest request, PackResvDto form) {

        //세션에서 맴버 정보 추출
        MemberResponseDto memberEntity = memberService.getMember(request);

        //임시 pk로 로그인

        String gender = "여";

        //ture = 남자 false = 여자
        if (memberEntity.isGender()) {

            gender = "남";
        }


        //form에서 패키지 엔티티 찾기
        Package packageEntity = packageRepository.findById(form.getPackagePk()).orElse(null);

        //패키지에서 여행일정 리스트 추출
        List<PackageSchedule> packageScheduleList = packageEntity.getPackageScheduleList();

        //폼에서 여행 출발일 추출
        LocalDate tripStart = form.getTripStart();

        //여행일정 pk 설정
        Long packageSchedulePk = 0L;

        PackageSchedule PackageScheduleCheck = new PackageSchedule();

        //여행출발일과 여행일정 리스트중에 출발일이 같은것을 찾기
        for (PackageSchedule packageScheduleDto : packageScheduleList) {

            log.info("여행 출국 날짜 : " + packageScheduleDto.getDepartureDateOut());

            //여행 출국날짜
            if (tripStart.isEqual(packageScheduleDto.getDepartureDateOut())) {

                log.info("여행 출국 날짜와 form 여행 출발일 같은것 : " + packageScheduleDto.getDepartureDateOut());
                log.info("여행 출발일  : " + form.getTripStart());

                packageSchedulePk = packageScheduleDto.getId();

                log.info(" packageSchedulePk : " + packageSchedulePk);

                PackageScheduleCheck = packageScheduleDto;
            }
        }

        if (packageSchedulePk == 0L) {

            log.error("!!!form의 여행 출발일과 여행일정 출발일이 같은게 없습니다!!!");

            return null;
        }


        //유류할증료 포함 추출
        BigDecimal fuelSurchargeIncluded = packageEntity.getFuelSurchargeIncluded();
        //유아 가격 추출
        BigDecimal babyPrice = packageEntity.getBabyPrice();


        //AdultCnt * fuelSurchargeIncluded 성인 합계 가격
        BigDecimal adultSumPrice = fuelSurchargeIncluded.multiply(BigDecimal.valueOf(form.getAdultCnt()));
        //ChildCnt * fuelSurchargeIncluded 아동 합계 가격
        BigDecimal childSumPrice = fuelSurchargeIncluded.multiply(BigDecimal.valueOf(form.getChildCnt()));
        //BabyCnt * babyPrice 유아 합계 가격
        BigDecimal babySumPrice = babyPrice.multiply(BigDecimal.valueOf(form.getBabyCnt()));

        //총 가격
        BigDecimal totalPrice = adultSumPrice
                .add(childSumPrice)
                .add(babySumPrice);

        //여행일정에서 가져온 여행 출발일 ("여행 출국 날짜")
        LocalDate tripStartDate = PackageScheduleCheck.getDepartureDateOut();
        //여행일정에서 가져온 여행 도착일 ("여행 귀국 도착 날짜")
        LocalDate tripEndDate = PackageScheduleCheck.getArrivalDateReturn();


        ResvPageDto resvPageDto = ResvPageDto.builder()
                //성인 유아 아동 인원수 및 출발일 패키지 회원pk
                .packResvDto(form)
                //여행 출발일 변환된것
                .tripStartString(Formatter.formatDateAndDay(tripStartDate))
                //여행 도착일 변환된것
                .tripEndString(Formatter.formatDateAndDay(tripEndDate))
                //여행 귀국 도착 날짜
                .tripEnd(PackageScheduleCheck.getArrivalDateReturn())
                //여행기간 (몇박 몇일)
                .tripDuration(Formatter.TripDuration(tripStartDate, tripEndDate))
                //회원 정보 Dto(memberResponseDto)
                .memberResponseDto(memberEntity)
                //회원 성별
                .gender(gender)
                //패키지 엔티티
                .packageEntity(packageEntity)
                //선택된 여행일정 엔티티
                .packageSchedule(PackageScheduleCheck)
                //휴대폰 번호 변환된것
                .phoneNum(Formatter.changePhoneNumber(memberEntity.getPhoneNumber()))
                //성인 합계 가격
                .adultSumPrice(adultSumPrice)
                //아동 합계 가격
                .childSumPrice(childSumPrice)
                //유아 합계 가격
                .babySumPrice(babySumPrice)
                //총 가격
                .totalPrice(totalPrice)

                //----formatter로 변환된 가격----

                //성인 합계 가격
                .adultSumPriceString(Formatter.changeBigDecimalFormat(adultSumPrice))
                //아동 합계 가격
                .childSumPriceString(Formatter.changeBigDecimalFormat(childSumPrice))
                //유아 합계 가격
                .babySumPriceString(Formatter.changeBigDecimalFormat(babySumPrice))
                //총 가격
                .totalPriceString(Formatter.changeBigDecimalFormat(totalPrice))

                .build();

        return resvPageDto;
    }

    //예약페이지에서 결제시 (카카오 페이 결제 승인 후)
    public HanSubmitCompleteDto ReservationSubmit(HttpServletRequest request, ResvSubmitDto form) {

        //세션에서 맴버 정보 추출
        MemberResponseDto memberDto = memberService.getMember(request);

        //추출한 맴버 키로 맴버 엔티티 가져옴
        Member memberEntity = memberRepository.findById(memberDto.getId()).orElse(null);

        //패키지 pk로 패키지 엔티티 가져옴
        Package PackageEntity = packageRepository.findById(form.getPackagePk()).orElse(null);

        //성인 아동 유아 수 세기
        int adultCnt = 0;
        int childCnt = 0;
        int babyCnt = 0;

        if (form.getAdult() != null) {
            adultCnt = form.getAdult().size();
            log.info("성인 수" + adultCnt);
        }
        if (form.getChild() != null) {
            childCnt = form.getChild().size();
            log.info("아동 수" + childCnt);
        }
        if (form.getBaby() != null) {
            babyCnt = form.getBaby().size();
            log.info("유아 수" + babyCnt);
        }


        //성인 아동 유아 총 가격 합산

        //유류 할증료 포함 가격
        BigDecimal fuelSurcharge = PackageEntity.getFuelSurchargeIncluded();
        //유아 가격
        BigDecimal BabyPrice = PackageEntity.getBabyPrice();


        //성인 총 가격 (유류할증료 포함 * 성인 수)
        BigDecimal adultSumPrice = fuelSurcharge.multiply(BigDecimal.valueOf(adultCnt));

        //아동 총 가격 (유류할증료 포함 * 아동 수)
        BigDecimal childSumPrice = fuelSurcharge.multiply(BigDecimal.valueOf(childCnt));

        //유아 총 가격
        BigDecimal babySumPrice = BabyPrice.multiply(BigDecimal.valueOf(babyCnt));


        // 1. 예약 먼저 저장
        Reservation reservation = Reservation.builder()
                //예약번호
                .code(Formatter.getReservationCode(LocalDateTime.now()))
                //패키지 엔티티
                .aPackage(PackageEntity)
                //회원 엔티티
                .member(memberEntity)
                //출발일자
                .startDate(form.getTripStart())
                //도착일자
                .endDate(form.getTripEnd())
                //예약상태 (예약 완료로 처리)
                .reservationState(ReservationState.CONFIRM)
                //요청 고유 번호 - 승인/취소가 구분된 결제번호
                .aid(form.getAid())
                //요청 고유 번호 - 승인/취소가 구분된 결제번호
                .tid(form.getTid())
                //결제 타입 (현금, 카드)
                .payType(form.getPayType())
                //결제 승인 시각
                .approvedAt(form.getApprovedAt())
                //카카오페이 발급사명
                .issuerCorp(form.getIssuerCorp())
                //카카오페이 매입사명
                .purCorp(form.getPurCorp())
                //카카오페이 매입사 코드
                .purCorpCode(form.getPurCorpCode())
                //총 결제 금액 (카카오 페이 api에서 받아온걸 사용)
                .totalPay(BigDecimal.valueOf(form.getAmount()))
                //성인 총 가격
                .adultSumPrice(adultSumPrice)
                //아동 총 가격
                .childSumPrice(childSumPrice)
                //유아 총 가격
                .babySumPrice(babySumPrice)


                .build();

        Reservation reservationEntity = hanReservationRepository.save(reservation); // 먼저 저장해서 ID 생성

        if (reservationEntity == null) {

            log.error("예약 엔티티 저장중 오류 발생");

            return null;

        }

        // 2. traveler들 하나로 합치기
        List<TravelerDto> allTravelers = new ArrayList<>();
        if (form.getAdult() != null) allTravelers.addAll(form.getAdult());
        if (form.getChild() != null) allTravelers.addAll(form.getChild());
        if (form.getBaby() != null) allTravelers.addAll(form.getBaby());

        // 3. 반복문으로 간단히 저장
        for (TravelerDto traveler : allTravelers) {
            //예약자 여부
            boolean resCheck = false;

            //세션의 회원 정보와 form의 여행자 정보가 이름 생년월일 성별이 같다면 그사람을 예약자로 저장
            if (traveler.getName().equals(memberEntity.getName()) && traveler.getBirth().equals(memberEntity.getBirth()) &&
                    traveler.getGender() == memberEntity.isGender()) {
                resCheck = true;
            }

            ReservationDetails detail = ReservationDetails.builder()
                    //예약 엔티티
                    .reservation(reservationEntity)
                    //여행자 이름
                    .name(traveler.getName())
                    //여행자 생년월일
                    .birth(traveler.getBirth())
                    //여행자 성별
                    .gender(Boolean.TRUE.equals(traveler.getGender())) // null-safe 처리
                    //여행자 핸드폰 번호 (null가능)
                    .phoneNumber(traveler.getPhone())
                    //연령대 성인 아동 유아
                    .ageGroup(traveler.getAgeGroup())
                    //예약자 여부 true = 예약자 false 예약자 x
                    .defaultReservation(resCheck)
                    //여권번호
                    .passportNum(traveler.getPassportNum())

                    .build();

            hanReservationDetailsRepository.save(detail);

        }

        //예약 완료 페이지로 보내줄 정보 담기
        HanSubmitCompleteDto hanSubmitCompleteDto = HanSubmitCompleteDto.builder()
                //예약 번호
                .code(reservationEntity.getCode())
                //패키지 명
                .packageName(reservationEntity.getAPackage().getPackageName())
                //출발일
                .startDate(Formatter.formatDateAndDay(reservationEntity.getStartDate()))
                //총 결제 금액
                .totalPay(Formatter.changeBigDecimalFormat(reservationEntity.getTotalPay()))

                .build();

        return hanSubmitCompleteDto;
    }


    //예약 목록 상세로 가는 서비스
    public ResDetailPageDto ResListDetail(HttpServletRequest request, Long id) {

        MemberResponseDto memberDto = memberService.getMember(request);

        //추출한 맴버 키로 맴버 엔티티 가져옴
        Member memberEntity = memberRepository.findById(memberDto.getId()).orElse(null);

        if (memberEntity == null) {
            log.error("맴버 엔티티 없음");
            return null;
        }

        //예약 목록 없을 시 실패
        if (memberEntity.getReservationList() == null && memberEntity.getReservationList().isEmpty()) {
            log.error("예약 목록 없음");
            return null;
        }

        //예약 pk로 예약 엔티티 들고오기
        Reservation reservation = hanReservationRepository.findById(id).orElse(null);

        if (reservation == null) {
            log.error("예약 엔티티 없음");
            return null;
        }

        List<ReservationDetails> ResList = reservation.getReservationDetailsList();

        if (ResList == null && ResList.isEmpty()) {
            log.error("예약 상세 엔티티 없음");
            return null;
        }
        //성인
        List<ResPeopleDto> resAdultList = new ArrayList<>();
        //아동
        List<ResPeopleDto> resChildList = new ArrayList<>();
        //유아
        List<ResPeopleDto> resBabyList = new ArrayList<>();

        //예약자
        ResPeopleDto resvMan = new ResPeopleDto();

        //예약상세를 성인 아동 유아 리스트로 변경
        for (ReservationDetails ResDto : ResList) {

            String gender = "여";

            //ture = 남자 false = 여자
            if (memberEntity.isGender()) {

                gender = "남";
            }


            ResPeopleDto resPeopleDto = ResPeopleDto.builder()
                    .reservationDetails(ResDto)
                    .gender(gender)
                    .birthString(Formatter.formatBirthDate(ResDto.getBirth()))
                    .build();

            //휴대폰 번호가 있을 시에만 휴대폰번호 변환해서 넣는다
            if (ResDto.getPhoneNumber() != null && !ResDto.getPhoneNumber().isEmpty()) {

                resPeopleDto.setPhoneString(Formatter.changePhoneNumber(ResDto.getPhoneNumber()));
            }

            //true일 경우 예약자
            if (ResDto.isDefaultReservation()) {

                log.info("예약자 : " + ResDto.getName());
                resvMan = resPeopleDto;

            }


            //연령대에 따라 다르게 넣어둔다
            switch (ResDto.getAgeGroup()) {

                //성인일시 성인 리스트에 넣어둔다
                case ADULT:
                    resAdultList.add(resPeopleDto);
                    break;

                //아동일시 아동 리스트에 넣어둔다
                case CHILD:
                    resChildList.add(resPeopleDto);
                    break;

                //유아일시 유아 리스트에 넣어둔다
                case INFANT:
                    resBabyList.add(resPeopleDto);
                    break;

                default:
                    log.error("switch 구문에서 에러발생");
                    break;

            }

        }


        //예약자 이름이 null일 경우
        if (resvMan == null ||
                resvMan.getReservationDetails() == null ||
                resvMan.getReservationDetails().getName() == null) {

            log.error("예약자가 없습니다");

            return null;
        }

        //예약에서 출발일 가져옴
        LocalDate startDate = reservation.getStartDate();

        //예약에서 여행 일정 가져옴
        List<PackageSchedule> packageScheduleList = reservation.getAPackage().getPackageScheduleList();


        //여행일정 pk 설정
        Long packageSchedulePk = 0L;

        PackageSchedule PackageScheduleCheck = new PackageSchedule();

        //여행출발일과 여행일정 리스트중에 출발일이 같은것을 찾기
        for (PackageSchedule packageScheduleDto : packageScheduleList) {

            log.info("여행 출국 날짜 : " + packageScheduleDto.getDepartureDateOut());

            //여행 출국날짜
            if (startDate.isEqual(packageScheduleDto.getDepartureDateOut())) {

                log.info("여행 출국 날짜와 form 여행 출발일 같은것 : " + packageScheduleDto.getDepartureDateOut());
                log.info("여행 출발일  : " + startDate);

                packageSchedulePk = packageScheduleDto.getId();

                log.info(" packageSchedulePk : " + packageSchedulePk);

                PackageScheduleCheck = packageScheduleDto;
            }
        }

        if (packageSchedulePk == 0L) {

            log.error("!!!form의 여행 출발일과 여행일정 출발일이 같은게 없습니다!!!");

            return null;
        }

        //여행 도착일
        LocalDate tripEndDate = PackageScheduleCheck.getArrivalDateReturn();

        //예약 취소 버튼
        boolean cancelButton = false;

        //예약 취소 요청중 버튼
        boolean cancelButtonReq = false;

        //리뷰 쓰기 버튼
        boolean reviewButton = false;

        //여행 출발일(startDate)이 오늘 날짜보다 미래인 경우 예약 취소 활성화
        if (startDate.isAfter(LocalDate.now())) {
            cancelButton = true;

        }

        //: 여행 출발일이 미래이고, 예약 상태가 **취소 요청 상태(REQUEST)**일 때  활성화
        if (startDate.isAfter(LocalDate.now()) && reservation.getReservationState() == REQUEST) {
            cancelButtonReq = true;

        }

        //여행 도착일 보다 현재 날짜가 미래이거나 같을때 리뷰 버튼 활성화
//        if (tripEndDate.isAfter(LocalDate.now()) || tripEndDate.isEqual(LocalDate.now())) {
//            reviewButton = true;
//
//        }

        //여행 도착일 보다 현재 날짜가 미래이거나 같을때 리뷰 버튼 활성화
        if (!tripEndDate.isAfter(LocalDate.now())) {
            log.info("여행 종료일 :" +tripEndDate);
            reviewButton = true;
        }


        ResDetailPageDto resDetailPageDto = ResDetailPageDto.builder()

                //예약 엔티티
                .reservation(reservation)
                //여행일정
                .packageSchedule(PackageScheduleCheck)
                //예약자 정보
                .resvMan(resvMan)
                //성인 인원수
                .adultCnt(resAdultList.size())
                //아동 인원수
                .childCnt(resChildList.size())
                //아이 인원수
                .babyCnt(resBabyList.size())
                //성인
                .resAdultList(resAdultList)
                //아동
                .resChildList(resChildList)
                //유아
                .resBabyList(resBabyList)
                //여행 출발일 변환된것
                .tripStartString(Formatter.formatDateAndDay(reservation.getStartDate()))
                //여행 도착일 변환된것
                .tripEndString(Formatter.formatDateAndDay(reservation.getEndDate()))
                //여행기간(몇박 몇일)
                .tripDuration(Formatter.TripDuration(startDate, tripEndDate))
                //카카오 페이 결제일 기준
                .resDate(Formatter.formatDateTimeWithDay(reservation.getApprovedAt()))
                //패키지 가격 (성인 1인 기준)
                .adultPriceString(Formatter.changeBigDecimalFormat(reservation.getAPackage().getFuelSurchargeIncluded()))
                //성인 총 결제 금액
                .adultSumPriceString(Formatter.changeBigDecimalFormat(reservation.getAdultSumPrice()))
                //아동 총 결제 금액
                .childSumPriceString(Formatter.changeBigDecimalFormat(reservation.getChildSumPrice()))
                //유아 총 결제 금액
                .babySumPriceString(Formatter.changeBigDecimalFormat(reservation.getBabySumPrice()))
                //총 결제 금액
                .totalPriceString(Formatter.changeBigDecimalFormat(reservation.getTotalPay()))
                //예약 취소 버튼 활성화 값
                .cancelButton(cancelButton)
                //예약 취소중 활성화 값
                .cancelButtonReq(cancelButtonReq)
                //리뷰 버튼 활성화 값
                .reviewButton(reviewButton)
                .build();

        return resDetailPageDto;
    }


}
