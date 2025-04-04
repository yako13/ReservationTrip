package Goods.Reservation_Trip.dto.review.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewResponseDto {

    //패키지 대표이미지 경로
    private String packageMainImage;

    //패키지 명
    private String packageName;

    //리뷰 내용
    private String content;

    //별점
    private int rating;

    //예약 PK
    private Long reservationId;

    //리뷰 PK
    private Long reviewId;

    //작성자 이름
    private String name;

    //작성 날짜
    private String createdAt;

    //이미지 url
    private List<String> imagesURL;

}
