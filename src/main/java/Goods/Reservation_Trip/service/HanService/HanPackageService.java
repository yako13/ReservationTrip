package Goods.Reservation_Trip.service.HanService;

import Goods.Reservation_Trip.dto.HanDto.PackPageDto;
import Goods.Reservation_Trip.entity.Package;
import Goods.Reservation_Trip.entity.PackageImage;
import Goods.Reservation_Trip.entity.PackageSchedule;
import Goods.Reservation_Trip.repository.HanPart.HanPackageScheduleRepository;
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
    private final MemberService memberService;
    private final HanReservationRepository hanReservationRepository;
    private final HanReservationDetailsRepository hanReservationDetailsRepository;
    private final HanPackageScheduleRepository hanPackageScheduleRepository;

    //상품 상세페이지로 가는 서비스
    public PackPageDto productDetailGo(HttpServletRequest request, Long id) {

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

                .build();


        return packPageDto;
    }

//    public PackPageDto PackageDateCheck(LocalDate selectedDate, Long id) {
//
//        //패키지 pk와 PackageStatus가 AVAILABLE 인것만 가져옴
//        List<PackageSchedule> packageScheduleList =
//                hanPackageScheduleRepository.findAvailableSchedules(id, AVAILABLE);
//
//        if (packageScheduleList == null) {
//            log.error("여행일정이 없거나 꽉찼습니다");
//            return null;
//
//        }
//
//        PackageSchedule packageScheduleCheck = new PackageSchedule();
//
//        boolean find = false;
//
//        //여행일정중 여행 출국 날짜와 선택한 날짜가 같은걸 찾기
//        for (PackageSchedule packageSchedule : packageScheduleList) {
//
//            if (selectedDate.isEqual(packageSchedule.getDepartureDateOut())) {
//
//                packageScheduleCheck = packageSchedule;
//
//                find = true;
//            }
//
//        }
//
//        //못찾았고 여행일정이 없거나 pk가 없을경우
//        if(!find ||  packageScheduleCheck.getId() ==null) {
//
//            log.error("여행일정이 없거나 비어있습니다");
//            return null;
//
//        }
//
//        //여행 출국 날짜 (여행 출발일) , 여행 출발 비행기 이륙 날짜
//        LocalDate tripStartUp = packageScheduleCheck.getDepartureDateOut();
//        //여행 출발 비행기 이륙 시간
//        LocalTime tripStartUpTime = packageScheduleCheck.getPackageScheduleDetails().getDepartureTimeOut();
//
//        // 여행 출발 비행기 착륙 날짜
//        LocalDate tripStartDown = packageScheduleCheck.getArrivalDateOut();
//        //여행 출발 비행기 착륙 시간
//        LocalTime tripStartDownTime = packageScheduleCheck.getPackageScheduleDetails().getArrivalTimeOut();
//
//        //여행 도착 비행기 이륙 날짜
//        LocalDate tripEndUp = packageScheduleCheck.getDepartureDateReturn();
//        LocalTime tripEndUpTime = packageScheduleCheck.getPackageScheduleDetails().getDepartureTimeReturn();
//
//        //여행 귀국 도착 날짜 (여행 도착일) , 여행 도착 비행기 착륙 날짜
//        LocalDate tripEndDown = packageScheduleCheck.getArrivalDateReturn();
//        LocalTime tripEndDownTime = packageScheduleCheck.getPackageScheduleDetails().getArrivalTimeReturn();
//
//
//
//
//
//    }


}
