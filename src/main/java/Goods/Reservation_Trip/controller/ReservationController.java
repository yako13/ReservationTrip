package Goods.Reservation_Trip.controller;

import Goods.Reservation_Trip.dto.HanDto.HeaderDto;
import Goods.Reservation_Trip.dto.member.res.MemberResponseDto;
import Goods.Reservation_Trip.dto.reservation.req.MemberReservationSearchDto;
import Goods.Reservation_Trip.dto.reservation.res.ReservationDetailsResponseDto;
import Goods.Reservation_Trip.dto.reservation.res.ReservationResponseDto;
import Goods.Reservation_Trip.dto.reservation.res.SalesDto;
import Goods.Reservation_Trip.service.HanService.HanHeaderService;
import Goods.Reservation_Trip.service.HanService.HanMemberService;
import Goods.Reservation_Trip.service.member.MemberService;
import Goods.Reservation_Trip.service.reservation.ReservationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    private final MemberService memberService;

    private final HanHeaderService hanHeaderService;

    /**
     * 관리자 차트
     */

    @GetMapping("/admin/chart")
    public String chartPage(
            @RequestParam(required = false) Integer month,
            @RequestParam(required = false) Integer year,
            Model model){

        int currentMonth = (month != null) ? month : LocalDate.now().getMonthValue();


        int currentYear = (month != null) ? year : LocalDate.now().getYear();

        List<SalesDto> dayChart = reservationService.getCheckoutChartDay(currentMonth,currentYear);
        List<SalesDto> monthChart = reservationService.getCheckoutChartMonth(currentYear);
        List<SalesDto> yearChart = reservationService.getCheckoutChartYear();
        List<SalesDto> dayOfWeekChart = reservationService.getCheckoutChartDayOfWeek(currentYear);
        List<SalesDto> weekendChart = reservationService.getCheckoutWeekend(currentYear);
        String totalSales = reservationService.totalSales();

        model.addAttribute("monthSelect",currentMonth);
        model.addAttribute("yearSelect",currentYear);

        model.addAttribute("month",monthChart);
        model.addAttribute("day",dayChart);
        model.addAttribute("year",yearChart);
        model.addAttribute("dayOfWeek",dayOfWeekChart);
        model.addAttribute("weekend",weekendChart);
        model.addAttribute("totalSales",totalSales);

        return "chart";
    }

    /**
     * 관리자 주문 리스트
     */
    @GetMapping("/admin/reservation/list")
    public String adminReservationListPage(@PageableDefault(size = 10) Pageable pageable,
                                           @RequestParam(defaultValue = "reservationCode") String search, //검색 기준
                                           @RequestParam(defaultValue = "0") int page, // 페이지 시작
                                           @RequestParam(defaultValue = "10") int size, // 예약 분류 기본 개수
                                           @RequestParam(defaultValue = "default") String sort, // 예약 정렬
                                           @RequestParam(defaultValue = "ALL") String reservationState, //예약 상태
                                           Model model) {

        Page<ReservationResponseDto> reservationResponseDtos = reservationService.pageReservation(page, size, sort, reservationState);


        model.addAttribute("reservationList", reservationResponseDtos.getContent());
        model.addAttribute("paging", reservationResponseDtos);
        model.addAttribute("total", reservationResponseDtos.getTotalElements());
        model.addAttribute("currentPage", reservationResponseDtos.getNumber());
        model.addAttribute("size", size);
        model.addAttribute("sortSelect", sort);
        model.addAttribute("searchSelect", search);
        model.addAttribute("reservationState", reservationState);

        return "reservation/adminList";
    }

    /**
     * 관리자 주문 검색 리스트
     */
    @GetMapping("/admin/reservation/search/index")
    public String adminSearchReservationListPage(@PageableDefault(size = 10) Pageable pageable,
                                                 @RequestParam(value = "keyword", required = false, defaultValue = "검색어를 입력해주세요.") String keyword,
                                                 @RequestParam(defaultValue = "reservationCode") String search, //검색 기준
                                                 @RequestParam(defaultValue = "0") int page, // 페이지 시작
                                                 @RequestParam(defaultValue = "10") int size, // 예약 분류 기본 개수
                                                 @RequestParam(defaultValue = "default") String sort, // 예약 정렬
                                                 @RequestParam(defaultValue = "ALL") String reservationState, //예약 상태
                                                 Model model) {

        Page<ReservationResponseDto> reservationResponseDtos = reservationService.pageReservationSearch(keyword, search, page, size, sort, reservationState);


        model.addAttribute("reservationList", reservationResponseDtos.getContent());
        model.addAttribute("paging", reservationResponseDtos);
        model.addAttribute("total", reservationResponseDtos.getTotalElements());
        model.addAttribute("currentPage", reservationResponseDtos.getNumber());
        model.addAttribute("size", size);
        model.addAttribute("sortSelect", sort);
        model.addAttribute("searchSelect", search);
        model.addAttribute("reservationState", reservationState);
        model.addAttribute("keywordQuery", keyword);

        return "reservation/adminSearchList";
    }

    /**
     * 관리자 주문 상세
     */
    @GetMapping("/admin/reservation/details/{id}")
    public String adminReservationDetailsPage(Model model, @PathVariable Long id){
        ReservationDetailsResponseDto reservationDetailsResponseDto = reservationService.getReservationDetails(id);
        model.addAttribute("reservationList",reservationDetailsResponseDto);
        return "reservation/adminDetails";
    }

    /**
     * 회원 주문 리스트
     */
    @GetMapping("/member/reservation/list")
    public String memberReservationListPage(HttpServletRequest request, Model model
    ) {
        HeaderDto headerDto = hanHeaderService.HeaderCategoryAndMember(request);

        model.addAttribute("headerDto",headerDto);
        MemberResponseDto memberResponseDto = memberService.getMember(request);
        List<ReservationDetailsResponseDto> reservationDetails = reservationService.getReservationList(memberResponseDto.getId());

        model.addAttribute("reservationList", reservationDetails);
        model.addAttribute("size",reservationDetails.size());
        return "reservation/memberList";
    }

    /**
     * 회원 주문 검색
     */
    @GetMapping("/member/reservation/search")
    public String memberReservationSearchPage(MemberReservationSearchDto searchDto,HttpServletRequest request,Model model){
        HeaderDto headerDto = hanHeaderService.HeaderCategoryAndMember(request);

        model.addAttribute("headerDto",headerDto);
        MemberResponseDto memberResponseDto = memberService.getMember(request);
        List<ReservationDetailsResponseDto> reservationDetails = reservationService.getReservationSearchList(memberResponseDto.getId(),searchDto);

        model.addAttribute("reservationList", reservationDetails);
        model.addAttribute("size",reservationDetails.size());
        model.addAttribute("searchSelect",searchDto.getSearchSelect());
        model.addAttribute("startDate",searchDto.getStartDate());
        model.addAttribute("endDate",searchDto.getEndDate());

        return "reservation/memberSearchList";
    }

    /**
     * 예약 상태 변경
     */
    @PostMapping("/admin/reservation/details/edit")
    @ResponseBody
    public String editReservationState(ReservationResponseDto reservationResponseDto){
        return reservationService.editReservationState(reservationResponseDto);
    }

    @PostMapping("/member/{reservationCode}/cancel")
    public String cancelReservation(@PathVariable String reservationCode, HttpServletRequest request, RedirectAttributes rttr) {
        MemberResponseDto memberResponseDto = memberService.getMember(request);
        reservationService.sendCancelNotification(reservationCode, memberResponseDto.getId());
        rttr.addFlashAttribute("data","예약 취소요청이 완료되었습니다.");
        return "redirect:/member/reservation/list";
    }

}
