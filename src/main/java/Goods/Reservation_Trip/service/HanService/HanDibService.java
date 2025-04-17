package Goods.Reservation_Trip.service.HanService;

import Goods.Reservation_Trip.dto.HanDto.DibPageDto;
import Goods.Reservation_Trip.dto.member.res.MemberResponseDto;
import Goods.Reservation_Trip.entity.Dib;
import Goods.Reservation_Trip.entity.Member;
import Goods.Reservation_Trip.entity.Package;
import Goods.Reservation_Trip.entity.PackageSchedule;
import Goods.Reservation_Trip.repository.HanPart.HanDibRepository;
import Goods.Reservation_Trip.repository.MemberRepository;
import Goods.Reservation_Trip.repository.aPackage.PackageRepository;
import Goods.Reservation_Trip.service.member.MemberService;
import Goods.Reservation_Trip.util.Formatter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static Goods.Reservation_Trip.enums.PackageStatus.AVAILABLE;

@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class HanDibService {

    private final MemberRepository memberRepository;
    private final PackageRepository packageRepository;

    private final MemberService memberService;
    private final HanMemberService hanMemberService;

    private final HanDibRepository hanDibRepository;


    //찜 목록으로 이동
    public List<DibPageDto> dibListGo(HttpServletRequest request) {

        //세션에서 맴버 정보 가져옴
        MemberResponseDto memberDto = memberService.getMember(request);

        if (memberDto == null) {
            log.error("세션에 문제가 있거나 로그인을 안했습니다 / HanDibService");
            return null;
        }

        Member memberEntity = memberRepository.findById(memberDto.getId()).orElse(null);

        if (memberEntity == null) {
            log.error("회원 정보를 찾을수 없습니다 / HanDibService");
            return null;
        }

        //장바구니 리스트 가져옴
        List<Dib> dibList = memberEntity.getDibList();

        //빈 dibPageDtoList 만듬
        List<DibPageDto> dibPageDtoList = new ArrayList<>();

        //찜 리스트가 있을경우에만
        if (dibList != null && !dibList.isEmpty()) {

            //찜 상품 가격을 변환 시키기위해 dto로 변환
            for (Dib dib : dibList) {

                LocalDate tripStart = dib.getTripStart();

                List<PackageSchedule> packageScheduleList = dib.getPackageEntity().getPackageScheduleList();

                //여행일정 pk 설정
                Long packageSchedulePk = 0L;

                PackageSchedule PackageScheduleCheck = new PackageSchedule();

                //여행출발일과 여행일정 리스트중에 출발일이 같은것을 찾기
                for (PackageSchedule packageScheduleDto : packageScheduleList) {

                    log.info("여행 출국 날짜 : " + packageScheduleDto.getDepartureDateOut());

                    //여행 출국날짜
                    if (tripStart.isEqual(packageScheduleDto.getDepartureDateOut())) {

                        log.info("여행 출국 날짜와 form 여행 출발일 같은것 : " + packageScheduleDto.getDepartureDateOut());
                        log.info("여행 출발일  : " + tripStart);

                        packageSchedulePk = packageScheduleDto.getId();

                        log.info(" packageSchedulePk : " + packageSchedulePk);

                        PackageScheduleCheck = packageScheduleDto;
                    }
                }

                if (packageSchedulePk == 0L) {

                    log.error("!!!form의 여행 출발일과 여행일정 출발일이 같은게 없습니다!!!");

                    return null;
                }

                boolean deadline = false;

                if (PackageScheduleCheck.getPackageStatus() != AVAILABLE) {
                    log.info("마감여부" + PackageScheduleCheck.getPackageStatus());
                    log.info("여행이 마감된 상태입니다");
                    deadline = true;

                }


                DibPageDto dibPageDto = DibPageDto.builder()

                        //찜 엔티티
                        .dib(dib)
                        //찜 목록에 있는 상품 가격을 변환
                        .price(Formatter.changeBigDecimalFormat(dib.getPackageEntity().getFuelSurchargeIncluded()))
                        //여행 시작일
                        .tripStart(Formatter.formatDateAndDay(tripStart))
                        //여행 종료일
                        .tripEnd(Formatter.formatDateAndDay(PackageScheduleCheck.getArrivalDateReturn()))
                        //마감여부
                        .deadline(deadline)

                        .build();

                dibPageDtoList.add(dibPageDto);

            }

        }

        return dibPageDtoList;

    }

    //찜 선택 삭제
    public void removeItem(Long dibId, HttpServletRequest request) {

        //세션에서 맴버 정보 가져옴
        MemberResponseDto memberDto = memberService.getMember(request);

        //세션의 맴버 이메일 정보추출
        String userEmail = memberDto.getEmail();

        Dib dib = hanDibRepository.findByIdAndMemberEmail(dibId, userEmail)
                .orElseThrow(() -> new AccessDeniedException("해당 찜 항목에 접근할 수 없습니다."));

        hanDibRepository.delete(dib);
    }

    //찜 전체 삭제
    public void removeAllByUser(HttpServletRequest request) {

        //세션에서 맴버 정보 가져옴
        MemberResponseDto memberDto = memberService.getMember(request);

        //세션의 맴버 이메일 정보추출
        String userEmail = memberDto.getEmail();

        List<Dib> userDibs = hanDibRepository.findAllByMemberEmail(userEmail);
        hanDibRepository.deleteAll(userDibs);
    }

    //찜 추가  (0 실패 1 성공)
    public int addDib(MemberResponseDto memberResponseDto, Long packagePk, LocalDate tripStart) {

        Member memberEntity = memberRepository.findById(memberResponseDto.getId()).orElse(null);

        if (memberEntity == null) {
            log.error("맴버 엔티티가 없습니다");
            return 0;
        }

        if (tripStart == null) {
            log.error("여행 시작일이 없습니다");
            return 0;
        }

        Package aPackage = packageRepository.findById(packagePk).orElse(null);

        if (aPackage == null) {
            log.error("패키지 엔티티가 없습니다");
            return 0;
        }

        // 이미 있는지 검사
        if (hanDibRepository.existsByMemberAndPackageEntity(memberEntity, aPackage)) {
            log.error("해당 찜이 있습니다");
            return 0;
        }

        List<PackageSchedule> packageScheduleList = aPackage.getPackageScheduleList();

        //여행일정 pk 설정
        Long packageSchedulePk = 0L;

        PackageSchedule PackageScheduleCheck = new PackageSchedule();

        //여행출발일과 여행일정 리스트중에 출발일이 같은것을 찾기
        for (PackageSchedule packageScheduleDto : packageScheduleList) {

            log.info("여행 출국 날짜 : " + packageScheduleDto.getDepartureDateOut());

            //여행 출국날짜
            if (tripStart.isEqual(packageScheduleDto.getDepartureDateOut())) {

                log.info("여행 출국 날짜와 form 여행 출발일 같은것 : " + packageScheduleDto.getDepartureDateOut());
                log.info("여행 출발일  : " + tripStart);

                packageSchedulePk = packageScheduleDto.getId();

                log.info(" packageSchedulePk : " + packageSchedulePk);

                PackageScheduleCheck = packageScheduleDto;
            }
        }

        if (packageSchedulePk == 0L) {

            log.error("!!!form의 여행 출발일과 여행일정 출발일이 같은게 없습니다!!!");

            return 2;
        }
        //예약 가능이 아닐경우 실패
        if (PackageScheduleCheck.getPackageStatus() != AVAILABLE) {

            log.error("예약이 꽉찼거나 불가합니다");

            return 3;
        }


        Dib dib = Dib.builder()
                .member(memberEntity)
                .packageEntity(aPackage)
                .tripStart(tripStart)
                .build();

        hanDibRepository.save(dib);

        return 1;

    }

    //찜 취소 (패키지 상세페이지에서 취소할경우)    (0 실패 1 성공)
    public int removeDib(MemberResponseDto memberResponseDto, Long packagePk) {

        Member memberEntity = memberRepository.findById(memberResponseDto.getId()).orElse(null);

        if (memberEntity == null) {
            log.error("맴버 엔티티가 없습니다");
            return 0;
        }

        Package aPackage = packageRepository.findById(packagePk).orElse(null);

        if (aPackage == null) {
            log.error("패키지 엔티티가 없습니다");
            return 0;
        }

        Dib dib = hanDibRepository.findByMemberAndPackageEntity(memberEntity, aPackage)
                .orElseThrow(() -> new RuntimeException("찜 항목 없음"));

        hanDibRepository.delete(dib);

        return 1;
    }

    //패키지 상세 들어갈때 찜 했는지 여부를 알려주는것
    public boolean packageDibCheck(HttpServletRequest request, Long packagePk) {

        MemberResponseDto memberResponseDto = hanMemberService.getMember(request);

        boolean isLiked = false;

        //세션이 있을경우
        if (memberResponseDto != null) {

            Member member = memberRepository.findById(memberResponseDto.getId()).orElse(null);

            //세션에 맴버 정보가 있을경우
            if (member != null) {

                Package aPackage = packageRepository.findById(packagePk).orElse(null);

                isLiked = hanDibRepository.existsByMemberAndPackageEntity(member, aPackage);
            }


        }

        return isLiked;
    }


}
