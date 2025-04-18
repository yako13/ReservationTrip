package Goods.Reservation_Trip.controller.HanController;

import Goods.Reservation_Trip.dto.HanDto.*;
import Goods.Reservation_Trip.service.HanService.HanHeaderService;
import Goods.Reservation_Trip.service.HanService.HanReservationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HanReservationController {

    private final HanReservationService hanReservationService;

    private final HanHeaderService hanHeaderService;

    //예약 페이지로 가기
    @GetMapping("/reservation")
    public String reservationGo(HttpServletRequest request, Model model, PackResvDto form,
                                RedirectAttributes rttr) {

        //헤더 카테고리랑 로그인 상태 및 유저이름 보내주는 서비스
        HeaderDto headerDto = hanHeaderService.HeaderCategoryAndMember(request);

        model.addAttribute("headerDto",headerDto);


        if (form == null || form.getPackagePk() == null) {

            log.error("form에 정보가 없습니다!!");

            rttr.addFlashAttribute("data", "잘못된 입력입니다");

            return "redirect:/";
        }

        if(form.getTripStart() ==null ){

            log.error("출발일 정보가 없습니다!!");
            rttr.addFlashAttribute("data", "출발일을 선택 하셔야 합니다");
            return "redirect:/";
        }

        if(form.getAdultCnt() <=0){

            log.error("성인이 한명도 없습니다 (예약자로 한명은 무조건 성인 이여야 합니다)");
            rttr.addFlashAttribute("data", "예약자 한명은 있어야 합니다");
            return "redirect:/";
        }

        if(form.getBabyCnt() <0 ||form.getChildCnt() <0){

            log.error("아동과 유아가 음수가 될수가 없습니다");
            rttr.addFlashAttribute("data", "에러가 발생했습니다 다시 시도해주세요");
            return "redirect:/";
        }


        log.info("여행출발일 : " + form.getTripStart());

        ResvPageDto resvPageDto = hanReservationService.ReservationPage(request, form);


        if (resvPageDto == null) {

            log.error("서비스에서 오류발생(hanReservationService.ReservationPage)");

            rttr.addFlashAttribute("data", "에러가 발생했습니다");

            return "redirect:/";
        }

        //로그인 안했을시
        if(resvPageDto.isLoginNo()){

            log.info("로그인 안함");

            rttr.addFlashAttribute("data", "로그인이 필요한 기능입니다");

            //원래 패키지 상세 페이지로 돌려보냄
            return "redirect:/package/details/" + form.getPackagePk();
        }

        //예약인원이 꽉찼을경우
        if(resvPageDto.isResvFull()){

            log.info("여행일정에 예약인원이 가득 찼습니다");
            rttr.addFlashAttribute("data", "죄송합니다. 해당 날짜의 예약 인원이 모두 마감되었습니다. 다른 날짜를 선택해 주세요.");

            return "redirect:/package/details/" + form.getPackagePk();
        }


        model.addAttribute("resvPageDto", resvPageDto);


        return "reservation/reservation";
    }

    //예약페이지에서 결제시
    @PostMapping("/reservation/submit")
    public String reservationSubmit(HttpServletRequest request, Model model, @ModelAttribute ResvSubmitDto form,
                                    RedirectAttributes rttr) {

        //헤더 카테고리랑 로그인 상태 및 유저이름 보내주는 서비스
        HeaderDto headerDto = hanHeaderService.HeaderCategoryAndMember(request);

        model.addAttribute("headerDto",headerDto);

        if (form == null || form.getPackagePk() == null) {

            log.error("form에 정보가 없습니다!!");

            rttr.addFlashAttribute("data", "잘못된 입력입니다");

            return "redirect:/";
        }

        // 예시 출력
        for (TravelerDto adult : form.getAdult()) {
            System.out.println("성인 이름: " + adult.getName());
            System.out.println("성인 성별: " + adult.getGender());
        }

        System.out.println("카카오 TID: " + form.getTid());

        HanSubmitCompleteDto hanSubmitCompleteDto = hanReservationService.ReservationSubmit(request, form);

        //null일경우 오류 발생 로그
        if (hanSubmitCompleteDto == null) {

            log.error("hanReservationService.ReservationSubmit 쪽 에러 발생");
            rttr.addFlashAttribute("data", "오류가 발생했습니다");

            return "redirect:/";
        }

        //로그인 안했을경우
        if (hanSubmitCompleteDto.isLoginNo()) {

            log.info("로그인 안함");
            rttr.addFlashAttribute("data", "로그인을 하셔야 예약이 가능합니다");

            return "redirect:/package/details/" + form.getPackagePk() ;
        }

        //예약이 꽉 찼을경우
        if (hanSubmitCompleteDto.isResvFull()) {

            log.info("예약이 꽉찼습니다");
            rttr.addFlashAttribute("data", "죄송합니다. 해당 날짜의 예약 인원이 모두 마감되었습니다. 다른 날짜를 선택해 주세요.");

            return "redirect:/package/details/" + form.getPackagePk()  ;
        }

        model.addAttribute("hanSubmitCompleteDto", hanSubmitCompleteDto);

        return "reservation/reservationComplete";
    }


    //---------------------회원 예약 상세----------------------------------------------

    //예약 상세 페이지
    @GetMapping("/member/reservation/details/{id}")
    public String reservationDetailGo(HttpServletRequest request, Model model, @PathVariable("id") Long id,
                                      RedirectAttributes rttr) {

        //헤더 카테고리랑 로그인 상태 및 유저이름 보내주는 서비스
        HeaderDto headerDto = hanHeaderService.HeaderCategoryAndMember(request);

        model.addAttribute("headerDto",headerDto);

        //pk가 null일경우
        if (id == null) {

            log.error("예약 상세 페이지로 가는 컨트롤러에서 에러발생 (Long id가 null입니다)");
            rttr.addFlashAttribute("data", "오류가 발생했습니다. 다시 시도해주세요");
            return "redirect:/";

        }

        ResDetailPageDto resDetailPageDto = hanReservationService.ResListDetail(request, id);

        //resDetailPageDto 가 null일 경우
        if (resDetailPageDto == null) {

            log.error("서비스에서 에러 발생 ");
            rttr.addFlashAttribute("data", "오류가 발생했습니다");
            return "redirect:/";

        }

        model.addAttribute("resDetailPageDto", resDetailPageDto);


        return "reservation/reservationDetail";
    }



}
