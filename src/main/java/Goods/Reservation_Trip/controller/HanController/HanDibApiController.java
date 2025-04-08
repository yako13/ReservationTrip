package Goods.Reservation_Trip.controller.HanController;

import Goods.Reservation_Trip.dto.member.res.MemberResponseDto;
import Goods.Reservation_Trip.service.HanService.HanDibService;
import Goods.Reservation_Trip.service.HanService.HanMemberService;
import Goods.Reservation_Trip.service.member.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class HanDibApiController {

    private final HanDibService hanDibService;
    private final MemberService memberService;
    private final HanMemberService hanMemberService;

    //개별 찜 삭제
    @PostMapping("/dib/delete")
    @ResponseBody
    public ResponseEntity<?> deleteDib(@RequestParam Long dibDel, Principal principal, HttpServletRequest request) {

        // 인증된 사용자 확인 및 소유 dib 확인
        hanDibService.removeItem(dibDel, request);

        return ResponseEntity.ok().build();
    }

    //찜 전체 삭제
    @PostMapping("/dib/deleteAll")
    @ResponseBody
    public ResponseEntity<?> deleteAllDibs(HttpServletRequest request) {

        hanDibService.removeAllByUser(request);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/dib/add")
    @ResponseBody
    public Map<String, String> addDib(@RequestParam Long packagePk,  @RequestParam String selectedDate, HttpServletRequest request) {
        Map<String, String> result = new HashMap<>();

         MemberResponseDto memberResponseDto = hanMemberService.getMember(request);

        LocalDate date = LocalDate.parse(selectedDate);

         //맴버 세션이 없을경우 로그인 하라고 알림
        if(memberResponseDto ==null){
            log.error("세션이 없습니다");
            result.put("status", "NOT_LOGGED_IN");
            return result;
        }


        int check = hanDibService.addDib(memberResponseDto, packagePk,date);

        //(0 실패 1 성공 2 여행일자가 없음, 3 여행일자가 꽉찼음)
        if (check == 0) {
            result.put("status", "NOT_LOGGED_IN");
            return result;
        }

        if (check == 2) {
            result.put("status", "NOT_DAY");
            return result;
        }

        if (check == 3) {
            result.put("status", "DAY_FULL");
            return result;
        }


        result.put("status", "OK");
        return result;
    }

    @PostMapping("/dib/cancel")
    @ResponseBody
    public Map<String, String> removeDib(@RequestParam Long packagePk, HttpServletRequest request) {
        Map<String, String> result = new HashMap<>();

        MemberResponseDto memberResponseDto = hanMemberService.getMember(request);

        //맴버 세션이 없을경우 로그인 하라고 알림
        if(memberResponseDto ==null){
            log.error("세션이 없습니다");
            result.put("status", "NOT_LOGGED_IN");
            return result;
        }


        int check = hanDibService.removeDib(memberResponseDto, packagePk);

        //(0 실패 1 성공)
        if (check != 1) {
            result.put("status", "NOT_LOGGED_IN");
            return result;
        }


        result.put("status", "OK");
        return result;
    }


}
