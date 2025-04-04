package Goods.Reservation_Trip.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ReviewController {

    @GetMapping("/member/review/new")
    public String memberReviewPage(){
        return "member/newReview";
    }
}
