package Goods.Reservation_Trip.controller;

import Goods.Reservation_Trip.dto.reservation.res.ReservationResponseDto;
import Goods.Reservation_Trip.service.reservation.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @GetMapping("/admin/reservation/list")
    public String adminReservationListPage(@PageableDefault(size = 10)Pageable pageable,
            @RequestParam(defaultValue = "ordererName") String search, //검색 기준
            @RequestParam(defaultValue = "0") int page, // 페이지 시작
            @RequestParam(defaultValue = "10") int size, // 예약 분류 기본 개수
            @RequestParam(defaultValue = "default") String sort, // 예약 정렬
            @RequestParam(defaultValue = "ALL") String reservationState, //예약 상태
            Model model){

        Page<ReservationResponseDto> reservationResponseDtos = reservationService.pageReservation(page,size,sort,reservationState);


        model.addAttribute("reservationList",reservationResponseDtos.getContent());
        model.addAttribute("paging",reservationResponseDtos);
        model.addAttribute("total", reservationResponseDtos.getTotalElements());
        model.addAttribute("currentPage", reservationResponseDtos.getNumber());
        model.addAttribute("size", size);
        model.addAttribute("sortSelect", sort);
        model.addAttribute("searchSelect",search);
        model.addAttribute("reservationState",reservationState);

        return "reservation/adminList";
    }
}
