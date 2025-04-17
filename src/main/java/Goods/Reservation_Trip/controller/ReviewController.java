package Goods.Reservation_Trip.controller;

import Goods.Reservation_Trip.dto.HanDto.HeaderDto;
import Goods.Reservation_Trip.dto.member.res.MemberResponseDto;
import Goods.Reservation_Trip.dto.reservation.res.ReservationDetailsResponseDto;
import Goods.Reservation_Trip.dto.review.req.ReviewDto;
import Goods.Reservation_Trip.dto.review.res.ReviewResponseDto;
import Goods.Reservation_Trip.service.HanService.HanHeaderService;
import Goods.Reservation_Trip.service.member.MemberService;
import Goods.Reservation_Trip.service.review.ReviewService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    private final MemberService memberService;

    private final HanHeaderService hanHeaderService;

    /**
     * 리뷰 등록 페이지
     */
    @GetMapping("/member/review/new/{reservationId}")
    public String memberReviewPage(Model model, @PathVariable Long reservationId,HttpServletRequest request) {

        HeaderDto headerDto = hanHeaderService.HeaderCategoryAndMember(request);

        model.addAttribute("headerDto",headerDto);

        ReviewResponseDto reviewResponseDto = reviewService.getReviewCreatingPage(reservationId);

        if (reviewResponseDto == null) return "redirect:/member/reservation/details/" + reservationId;

        model.addAttribute("packageMainImage", reviewResponseDto.getPackageMainImage());
        model.addAttribute("packageName", reviewResponseDto.getPackageName());
        model.addAttribute("reservationId", reservationId);
        model.addAttribute("packagePK",reviewResponseDto.getPackagePK());

        return "review/new";
    }

    /**
     * 리뷰 등록
     */
    @PostMapping("/member/review/new")
    public String createReview(ReviewDto reviewDto, HttpServletRequest request) {
        MemberResponseDto memberResponseDto = memberService.getMember(request);
        reviewService.registerReview(reviewDto, memberResponseDto.getId());
        return "redirect:/member/review/list";
    }

    /**
     * 리뷰 수정 페이지
     */
    @GetMapping("/member/review/edit/{reviewId}")
    public String reviewEditPage(Model model, @PathVariable Long reviewId,HttpServletRequest request) {

        HeaderDto headerDto = hanHeaderService.HeaderCategoryAndMember(request);

        model.addAttribute("headerDto",headerDto);

        ReviewResponseDto reviewResponseDto = reviewService.getReviewEditPage(reviewId);
        model.addAttribute("packageMainImage", reviewResponseDto.getPackageMainImage());
        model.addAttribute("packageName", reviewResponseDto.getPackageName());
        model.addAttribute("content", reviewResponseDto.getContent());
        model.addAttribute("reviewImages", reviewResponseDto.getReviewImageList());
        model.addAttribute("rating", reviewResponseDto.getRating());
        model.addAttribute("reviewId", reviewId);
        model.addAttribute("packagePK",reviewResponseDto.getPackagePK());

        return "review/edit";
    }

    /**
     * 리뷰 수정
     */
    @PostMapping("/member/review/edit")
    public String editReview(ReviewDto reviewDto, HttpServletRequest request,
                             @RequestParam(value = "deletedImages", required = false) String deletedImages
    ) {
        MemberResponseDto memberResponseDto = memberService.getMember(request);

        reviewService.editReview(reviewDto, memberResponseDto.getId(), deletedImages);

        return "redirect:/member/review/list";
    }

    /**
     * 작성가능한 리뷰 페이지
     */
    @GetMapping("/member/review/list/able")
    public String memberReviewAbleListPage(Model model, HttpServletRequest request) {
        HeaderDto headerDto = hanHeaderService.HeaderCategoryAndMember(request);

        model.addAttribute("headerDto",headerDto);
        MemberResponseDto memberResponseDto = memberService.getMember(request);

        List<ReservationDetailsResponseDto> responseDtoList = reviewService.getReviewAblePage(memberResponseDto.getId());

        model.addAttribute("reservationList", responseDtoList);

        return "review/able";
    }

    @GetMapping("/member/review/list")
    public String memberReviewListPage(Model model, HttpServletRequest request) {
        HeaderDto headerDto = hanHeaderService.HeaderCategoryAndMember(request);

        model.addAttribute("headerDto",headerDto);
        MemberResponseDto memberResponseDto = memberService.getMember(request);
        List<ReviewResponseDto> reviewResponseDtoList = reviewService.getReviewList(memberResponseDto.getId());

        model.addAttribute("reviewList", reviewResponseDtoList);

        return "review/list";
    }

    @PostMapping("/member/review/delete/{id}")
    @ResponseBody
    public String deleteMemberReview(@PathVariable Long id, HttpServletRequest request) {
        MemberResponseDto memberResponseDto = memberService.getMember(request);

        if (reviewService.deleteReview(id, memberResponseDto.getId()).equals("500")) return "500";

        return "1000";
    }
}
