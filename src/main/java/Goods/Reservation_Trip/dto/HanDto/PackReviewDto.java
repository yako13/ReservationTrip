package Goods.Reservation_Trip.dto.HanDto;

import Goods.Reservation_Trip.entity.Review;
import lombok.*;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PackReviewDto {

    //리뷰 엔티티
    private List<Review> reviewList;

    //리뷰 하나라도 있는지 체크  T = 리뷰 없음 F = 리뷰 있음
    private boolean reviewNo;

    //리뷰 수
    private int reviewCnt;


}
