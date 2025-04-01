package Goods.Reservation_Trip.controller;

import Goods.Reservation_Trip.dto.member.res.MemberResponseDto;
import Goods.Reservation_Trip.dto.reservation.res.ReservationDetailsResponseDto;
import Goods.Reservation_Trip.dto.reservation.res.ReservationResponseDto;
import Goods.Reservation_Trip.service.member.MemberService;
import Goods.Reservation_Trip.service.reservation.ReservationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    private final MemberService memberService;

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
     * 회원 주문 리스트
     */
    @GetMapping("/member/reservation/list")
    public String memberReservationListPage(HttpServletRequest request, Model model, @RequestParam(required = false) Integer month,
                                            @RequestParam(required = false) Integer year
    ) {

        int currentMonth = (month != null) ? month : LocalDate.now().getMonthValue();


        int currentYear = (month != null) ? year : LocalDate.now().getYear();


        model.addAttribute("monthSelect", currentMonth);
        model.addAttribute("yearSelect", currentYear);

        MemberResponseDto memberResponseDto = memberService.getMember(request);
        List<ReservationDetailsResponseDto> reservationDetails = reservationService.getReservationDetails(memberResponseDto.getId(),year,month);

        model.addAttribute("reservationList", reservationDetails);
        return "reservation/memberList";
    }
}
