package Goods.Reservation_Trip.controller;

import Goods.Reservation_Trip.dto.member.res.MemberResponseDto;
import Goods.Reservation_Trip.dto.review.req.ReviewDto;
import Goods.Reservation_Trip.dto.review.res.ReviewResponseDto;
import Goods.Reservation_Trip.service.member.MemberService;
import Goods.Reservation_Trip.service.review.ReviewService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    private final MemberService memberService;

    @GetMapping("/member/review/new/{reservationId}")
    public String memberReviewPage(Model model, @PathVariable Long reservationId, HttpServletRequest request){
        MemberResponseDto memberResponseDto = memberService.getMember(request);

        ReviewResponseDto reviewResponseDto =reviewService.getReviewCreatingPage(reservationId,memberResponseDto.getId());

        if(reviewResponseDto == null) return "redirect:/member/reservation/details/"+reservationId;

        model.addAttribute("packageMainImage",reviewResponseDto.getPackageMainImage());
        model.addAttribute("packageName",reviewResponseDto.getPackageName());
        model.addAttribute("reservationId",reservationId);

        return "member/newReview";
    }

    @PostMapping("/member/review/new")
    public String createReview(ReviewDto reviewDto,HttpServletRequest request){
        MemberResponseDto memberResponseDto = memberService.getMember(request);
        reviewService.registerReview(reviewDto,memberResponseDto.getId());
//        return "redirect:/member/review/list";
        return "redirect:/member/reservation/list";
    }
}
