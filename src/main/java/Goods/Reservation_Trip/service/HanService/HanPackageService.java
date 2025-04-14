package Goods.Reservation_Trip.service.HanService;

import Goods.Reservation_Trip.dto.HanDto.PackApiDto;
import Goods.Reservation_Trip.dto.HanDto.PackCategoryDto;
import Goods.Reservation_Trip.dto.HanDto.PackPageDto;
import Goods.Reservation_Trip.dto.HanDto.PackPageListDto;
import Goods.Reservation_Trip.dto.member.res.MemberResponseDto;
import Goods.Reservation_Trip.entity.*;
import Goods.Reservation_Trip.entity.Package;
import Goods.Reservation_Trip.enums.PackageStatus;
import Goods.Reservation_Trip.repository.HanPart.*;
import Goods.Reservation_Trip.repository.MemberRepository;
import Goods.Reservation_Trip.repository.aPackage.PackageRepository;
import Goods.Reservation_Trip.util.Formatter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static Goods.Reservation_Trip.enums.PackageImageType.DESC;
import static Goods.Reservation_Trip.enums.PackageStatus.AVAILABLE;


@Slf4j
@Service
@RequiredArgsConstructor
public class HanPackageService {

    private final MemberRepository memberRepository;
    private final PackageRepository packageRepository;
    private final HanPackageRepository hanPackageRepository;
    private final HanReservationRepository hanReservationRepository;
    private final HanReservationDetailsRepository hanReservationDetailsRepository;
    private final HanPackageScheduleRepository hanPackageScheduleRepository;
    private final HanMemberService hanMemberService;
    private final HanAirportRepository hanAirportRepository;


    //상품 상세페이지로 가는 서비스
    public PackPageDto productDetailGo(HttpServletRequest request, Long id) {

        //세션에서 맴버 정보 추출
        MemberResponseDto memberEntity = hanMemberService.getMember(request);

        boolean loginYes = true;

        if (memberEntity == null) {
            log.info("회원정보가 없거나 로그인을 안했습니다");
            loginYes = false;
        }


        Package PackageEntity = packageRepository.findById(id).orElse(null);

        //패키지 엔티티가 없을경우
        if (PackageEntity == null) {

            log.error("패키지 엔티티가 없습니다");
            return null;

        }

        //패키지 pk와 PackageStatus가 AVAILABLE 인것만 가져옴
        List<PackageSchedule> packageScheduleList =
                hanPackageScheduleRepository.findAvailableSchedules(id, AVAILABLE);

        //여행 일정이 없을경우
        if (packageScheduleList == null || packageScheduleList.isEmpty()) {

            log.error("여행 일정이 없습니다");
            return null;

        }

        PackageSchedule packageSchedule = packageScheduleList.get(0);

        //예약 최대 맴버
        int maxMember = packageSchedule.getMaximumMember();

        //예약 중인 맴버
        int resvMember = packageSchedule.getReservedMemberCount();

        //예약 가능 인원 (예약 최대 맴버 - 예약 중인 맴버)
        int resvOkMember = maxMember - resvMember;

        //예약 만석 여부
        boolean resvFull = false;

        //예약 최대 맴버 보다 예약 중인 맴버가 더 클경우
        if (maxMember <= resvMember) {

            resvFull = true;

        }


        //여행 출국 날짜 (여행 출발일) , 여행 출발 비행기 이륙 날짜
        LocalDate tripStartUp = packageSchedule.getDepartureDateOut();
        //여행 출발 비행기 이륙 시간
        LocalTime tripStartUpTime = packageSchedule.getPackageScheduleDetails().getDepartureTimeOut();

        // 여행 출발 비행기 착륙 날짜
        LocalDate tripStartDown = packageSchedule.getArrivalDateOut();
        //여행 출발 비행기 착륙 시간
        LocalTime tripStartDownTime = packageSchedule.getPackageScheduleDetails().getArrivalTimeOut();

        //여행 도착 비행기 이륙 날짜
        LocalDate tripEndUp = packageSchedule.getDepartureDateReturn();
        LocalTime tripEndUpTime = packageSchedule.getPackageScheduleDetails().getDepartureTimeReturn();

        //여행 귀국 도착 날짜 (여행 도착일) , 여행 도착 비행기 착륙 날짜
        LocalDate tripEndDown = packageSchedule.getArrivalDateReturn();
        LocalTime tripEndDownTime = packageSchedule.getPackageScheduleDetails().getArrivalTimeReturn();


        List<LocalDate> availableDateEntityList = hanPackageScheduleRepository.findDepartureDatesByPackageIdAndStatus(id, AVAILABLE);

        if (availableDateEntityList == null || availableDateEntityList.isEmpty()) {

            log.error("여행 일정 날짜 들고오는곳에서 에러가 발생했습니다");
            return null;

        }

        List<String> availableDateList = new ArrayList<>();

        //LocalDate를 String으로 변환
        for (LocalDate localDate : availableDateEntityList) {

            log.info("여행 가능일 : " + localDate);

            availableDateList.add(localDate.toString());

        }

        List<PackageImage> packageImageList = PackageEntity.getPackageImageList();

        //캐러셀 이미지 담을 리스트
        List<PackageImage> carouselImgList = new ArrayList<>();
        //상품 설명 이미지 담을 리스트
        List<PackageImage> infoImgList = new ArrayList<>();

        //반복문으로 캐러셀과 상품 설명을 각각 나눠 담는다
        for (PackageImage packageImage : packageImageList) {

            if (packageImage != null && packageImage.getPackageImageType() == DESC) {
                infoImgList.add(packageImage);

            } else if (packageImage != null) {

                carouselImgList.add(packageImage);
            }

        }
        boolean carouselImgYes = true;
        boolean infoImgYes = true;
        boolean carouselImgOne = true;

        //캐러셀 이미지가 하나도 없을경우
        if (carouselImgList.isEmpty()) {
            carouselImgYes = false;
        }

        //캐러셀 이미지가 한장일경우
        if (carouselImgList.size() == 1) {

            carouselImgOne = false;
        }

        //상세 설명 이미지가 하나도 없을경우
        if (infoImgList.isEmpty()) {
            infoImgYes = false;
        }

        PackPageDto packPageDto = PackPageDto.builder()

                //패키지 엔티티
                .packageEntity(PackageEntity)
                //여행 일정 디테일 (인덱스 0번 꺼의 디테일 / 맨처음 패키지 상세 갈시)
                .packageScheduleDetails(packageSchedule.getPackageScheduleDetails())
                //성인 가격 변환한것 유류할증료 포함 가격 (fuelSurchargeIncluded)
                .adultPriceString(Formatter.BigDecimalFormat(PackageEntity.getFuelSurchargeIncluded()))
                //아동 가격 변환한것 유류할증료 포함 가격 (fuelSurchargeIncluded)
                .childPriceString(Formatter.BigDecimalFormat(PackageEntity.getFuelSurchargeIncluded()))
                //유아 가격 변환한것
                .babyPriceString(Formatter.BigDecimalFormat(PackageEntity.getBabyPrice()))
                //유류할증료
                .fuelSurcharge(Formatter.BigDecimalFormat(PackageEntity.getFuelSurcharge()))
                //여행 일정
                .tripDate(Formatter.TripDuration(tripStartUp, tripEndDown))

                //여행 출발 비행기 이륙 날짜 및 시간
                .tripStartUp(Formatter.formatDayAndTime(tripStartUp, tripStartUpTime))

                //여행 출발 비행기 착륙 날짜 및 시간
                .tripStartDown(Formatter.formatDayAndTime(tripStartDown, tripStartDownTime))

                //여행 도착 비행기 이륙 날짜 및 시간
                .tripEndUp(Formatter.formatDayAndTime(tripEndUp, tripEndUpTime))

                //여행 도착 비행기 착륙 날짜 및 시간
                .tripEndDown(Formatter.formatDayAndTime(tripEndDown, tripEndDownTime))

                //여행 가능날짜 모음
                .availableDateList(availableDateList)
                //캐러셀 이미지 모음
                .carouselImg(carouselImgList)
                //상품 설명 이미지 모음
                .infoImg(infoImgList)

                //캐러셀 이미지 유무체크
                .carouselImgYes(carouselImgYes)
                //캐러셀 이미지가 한장만 있는지 체크
                .carouselImgOne(carouselImgOne)
                //상세 설명 이미지 유무체크
                .infoImgYes(infoImgYes)

                //로그인 유무 T = 로그인 F = 비로그인
                .loginYes(loginYes)
                //예약 만석 여부
                .resvFull(resvFull)

                //최대 예약인원
                .resvMember(resvMember)
                //최소 예약인원
                .minMember(packageSchedule.getMinimumRequired())
                //예약가능 인원
                .resvOkMember(resvOkMember)

                .build();


        return packPageDto;
    }

    public PackApiDto PackageDateCheck(LocalDate selectedDate, Long id) {

        //패키지 pk와 PackageStatus가 AVAILABLE 인것만 가져옴
        List<PackageSchedule> packageScheduleList =
                hanPackageScheduleRepository.findAvailableSchedules(id, AVAILABLE);

        if (packageScheduleList == null) {
            log.error("여행일정이 없거나 꽉찼습니다");
            return null;

        }

        PackageSchedule packageScheduleCheck = new PackageSchedule();

        boolean find = false;

        //여행일정중 여행 출국 날짜와 선택한 날짜가 같은걸 찾기
        for (PackageSchedule packageSchedule : packageScheduleList) {

            if (selectedDate.isEqual(packageSchedule.getDepartureDateOut())) {

                packageScheduleCheck = packageSchedule;

                find = true;
            }

        }

        //못찾았고 여행일정이 없거나 pk가 없을경우
        if (!find || packageScheduleCheck.getId() == null) {

            log.error("여행일정이 없거나 비어있습니다");

            PackApiDto packApiDto = PackApiDto.builder()
                    .resvYes(false)
                    .build();

            return packApiDto;

        }

        //최대 예약 가능 맴버
        int maxMember = packageScheduleCheck.getMaximumMember();
        //최소 예약 가능 맴버
        int minMember = packageScheduleCheck.getMinimumRequired();
        //예약한 인원 수
        int resvedMember = packageScheduleCheck.getReservedMemberCount();

        //예약 가능 인원수 = 최대 예약 가능 맴버 - 예약한 인원 수
        int resvOkMember = maxMember - resvedMember;

        //최대 인원보다 예약된 인원이 더 많거나 같을경우 실패
        if (maxMember <= resvedMember) {

            log.error("예약인원이 이미 꽉찼습니다");

            PackApiDto packApiDto = PackApiDto.builder()
                    .resvYes(false)
                    .build();

            return packApiDto;

        }


        //여행 출국 날짜 (여행 출발일) , 여행 출발 비행기 이륙 날짜
        LocalDate tripStartUp = packageScheduleCheck.getDepartureDateOut();
        //여행 출발 비행기 이륙 시간
        LocalTime tripStartUpTime = packageScheduleCheck.getPackageScheduleDetails().getDepartureTimeOut();

        // 여행 출발 비행기 착륙 날짜
        LocalDate tripStartDown = packageScheduleCheck.getArrivalDateOut();
        //여행 출발 비행기 착륙 시간
        LocalTime tripStartDownTime = packageScheduleCheck.getPackageScheduleDetails().getArrivalTimeOut();

        //여행 도착 비행기 이륙 날짜
        LocalDate tripEndUp = packageScheduleCheck.getDepartureDateReturn();
        LocalTime tripEndUpTime = packageScheduleCheck.getPackageScheduleDetails().getDepartureTimeReturn();

        //여행 귀국 도착 날짜 (여행 도착일) , 여행 도착 비행기 착륙 날짜
        LocalDate tripEndDown = packageScheduleCheck.getArrivalDateReturn();
        LocalTime tripEndDownTime = packageScheduleCheck.getPackageScheduleDetails().getArrivalTimeReturn();


        PackApiDto packApiDto = PackApiDto.builder()
                //여행 출국 비행기
                .startAirplane(packageScheduleCheck.getPackageScheduleDetails().getAirlineOut().getName())
                //여행 귀국 비행기
                .endAirplane(packageScheduleCheck.getPackageScheduleDetails().getAirlineReturn().getName())
                //여행 기간 변환
                .tripDate(Formatter.TripDuration(tripStartUp, tripEndDown))

                //여행 출발 비행기 이륙 날짜 및 시간
                .tripStartUp(Formatter.formatDayAndTime(tripStartUp, tripStartUpTime))

                //여행 출발 비행기 착륙 날짜 및 시간
                .tripStartDown(Formatter.formatDayAndTime(tripStartDown, tripStartDownTime))

                //여행 도착 비행기 이륙 날짜 및 시간
                .tripEndUp(Formatter.formatDayAndTime(tripEndUp, tripEndUpTime))

                //여행 도착 비행기 착륙 날짜 및 시간
                .tripEndDown(Formatter.formatDayAndTime(tripEndDown, tripEndDownTime))


                //예약 가능 여부
                .resvYes(true)

                //이미 예약한 인원
                .resvPeople(resvedMember)
                //예약 가능 인원
                .resvOkPeople(resvOkMember)
                //최소 예약 인원
                .resvMinPeople(minMember)

                .build();


        return packApiDto;
    }


//    public List<Package> filterPackages(Boolean airfare, Boolean hotelFee, Boolean guide, Boolean noShopping,
//                                        LocalDate startDate, LocalDate endDate) {
//        return hanPackageRepository.filterPackagesWithConditions(airfare, hotelFee, guide, noShopping, startDate, endDate);
//    }

    //상품 리스트로 가는 서비스 신버전 (카테고리랑 필터에 따라서)
    public Page<PackPageListDto> filterPackagesWithPaging(PackCategoryDto dto, int page, int size) {

        log.info("쇼핑" + dto.getNoShopping());

        //쇼핑 여부를 반대로 뒤집어주기위한 함수 (true > false)
        if (dto.getNoShopping() != null && dto.getNoShopping()) {

            dto.setNoShopping(false);

        }


        Sort sort = Sort.by(Sort.Direction.ASC, "id");

        String sortType = "";

        if (dto.getSortType() == null) {
            dto.setSortType(sortType);
        }

        // 정렬 조건 설정
        switch (dto.getSortType()) {
            case "rating":
                sort = Sort.by(Sort.Direction.DESC, "averageRating");
                break;

            case "highPrice":
                sort = Sort.by(Sort.Direction.DESC, "fuelSurchargeIncluded");
                break;

            case "lowPrice":
                sort = Sort.by(Sort.Direction.ASC, "fuelSurchargeIncluded");
                break;

            default:
                // 아무 정렬도 안 했을 경우 기본 정렬 유지
                break;
        }

        if (dto.getStartDate() != null && dto.getEndDate() != null) {
            // 날짜 필터 적용
        } else {
            // 날짜 조건 없이 전체 조회
        }

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Package> entityPage;


        // 3. 예약 많은 순 정렬일 경우 별도 쿼리 실행
        if ("reservation".equals(dto.getSortType())) {
            entityPage = hanPackageRepository.filterPackagesWithConditions(
                    dto.getAirfare(), dto.getHotelFee(), dto.getGuide(), dto.getNoShopping(),
                    dto.getStartDate(), dto.getEndDate(), dto.getCategoryId(),dto.getCategoryDepth(),
                    dto.getPeriod(), dto.getKeyword(),  dto.getAirportId(), pageable
            );
        } else {
            entityPage = hanPackageRepository.filterByReservationCount(
                    dto.getAirfare(), dto.getHotelFee(), dto.getGuide(), dto.getNoShopping(),
                    dto.getStartDate(), dto.getEndDate(),dto.getCategoryId(),dto.getCategoryDepth(),
                    dto.getPeriod(), dto.getKeyword(),  dto.getAirportId(), pageable
            );

        }

        // 4. entity → dto 변환
        List<PackPageListDto> dtoList = new ArrayList<>();
        for (Package pack : entityPage.getContent()) {
            dtoList.add(PackPageListDto.fromEntity(pack));
        }

        return new PageImpl<>(dtoList, pageable, entityPage.getTotalElements());
    }


    //상품 리스트로 가는 서비스 구버전
    public Page<PackPageListDto> packageListGo(int page, int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
        Page<Package> entityPage = hanPackageRepository.findByPackageStatus(PackageStatus.AVAILABLE, pageable);

        //페이지 네이션 하기위한 dto리스트선언
        List<PackPageListDto> dtoList = new ArrayList<>();

        //패이지 네이션 되어 있는 패키지 리스트를 dto 페이지 네이션으로 변환
        for (Package packageDto : entityPage.getContent()) {
            //dto에 있는 메서드로 변환
            dtoList.add(PackPageListDto.fromEntity(packageDto));
        }

        //실제 데이터 목록     //페이지 요청정보  //전체 데이터 개수
        return new PageImpl<>(dtoList, pageable, entityPage.getTotalElements());

    }

    //공항 카테고리 가져오는 서비스
    public List<Airport> AirportCategory() {

        List<Airport> airportList = hanAirportRepository.findAll();

        if (airportList == null) {

            log.error("공항 정보가 하나도 없습니다");
            return null;

        }

        if (airportList.isEmpty()) {

            log.error("공항 정보가 하나도 없습니다");
            return null;

        }


        return airportList;
    }


    //선택한 공항 이름 가져오는 서비스
    public String AirportCategoryName(PackCategoryDto dto) {

        if (dto.getAirportId() != null) {

            Airport airport = hanAirportRepository.findById(dto.getAirportId()).orElse(null);

            if (airport != null) {
                return airport.getName();
            }
        }

        //없을경우
        return "전체";
    }


}
