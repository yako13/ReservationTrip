package Goods.Reservation_Trip.dto.review.req;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewDto {

    //리뷰 PK
    private Long reservationId;

    //리뷰 내용
    private String content;

    //리뷰 별점
    private int rating;

    //리뷰 이미지
    private List<MultipartFile> reviewImages;

}
