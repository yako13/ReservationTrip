package Goods.Reservation_Trip.service.HanService;

import Goods.Reservation_Trip.dto.HanDto.HeaderDto;
import Goods.Reservation_Trip.dto.member.res.MemberResponseDto;
import Goods.Reservation_Trip.entity.PackageCategory;
import Goods.Reservation_Trip.repository.HanPart.HanPackageCategoryRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class HanHeaderService {

    private final HanPackageCategoryRepository hanPackageCategoryRepository;

    private final HanMemberService hanMemberService;

    //헤더에 카테고리랑 로그인 유무 맴버 정보 넣어주는곳
    public HeaderDto HeaderCategoryAndMember(HttpServletRequest request) {

//        List<PackageCategory> mainList = hanPackageCategoryRepository.findByDepth(1);

        //한국이 아닌 카테고리 가져옴
        List<PackageCategory> mainList = hanPackageCategoryRepository.findByDepthAndNameNot(1,"한국");

        //대분류가 없을경우
        if(mainList ==null && mainList.isEmpty() ){

            log.error("카테고리가 없거나 대분류가 없습니다");

        }


        //세션에서 맴버 정보 추출
        MemberResponseDto memberEntity = hanMemberService.getMember(request);

        if (memberEntity == null) {
            log.info("회원정보가 없거나 로그인을 안했습니다");


            HeaderDto headerDto = HeaderDto.builder()
                    //카테고리
                    .mainList(mainList)
                    //로그인 여부
                    .loginYes(false)
                    //유저 이름
                    .name("비로그인")

                    .build();

            return headerDto;

        }

        HeaderDto headerDto = HeaderDto.builder()
                //카테고리
                .mainList(mainList)
                //로그인 여부
                .loginYes(true)
                //유저 이름
                .name(memberEntity.getName())

                .build();


//        for (PackageCategory main : mainList) {
//            log.info("대분류: " + main.getName());
//
//
//            for (PackageCategory sub : main.getChildren()) {
//                log.info("  중분류: " + sub.getName());
//
//
//                for (PackageCategory small : sub.getChildren()) {
//                    log.info("    소분류: " + small.getName());
//
//                }
//            }

        return headerDto;
    }


}



