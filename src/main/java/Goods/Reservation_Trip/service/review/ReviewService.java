package Goods.Reservation_Trip.service.review;

import Goods.Reservation_Trip.config.ImageManager;
import Goods.Reservation_Trip.dto.review.req.ReviewDto;
import Goods.Reservation_Trip.dto.review.res.ReviewResponseDto;
import Goods.Reservation_Trip.entity.Reservation;
import Goods.Reservation_Trip.entity.Review;
import Goods.Reservation_Trip.entity.ReviewImage;
import Goods.Reservation_Trip.repository.ReservationRepository;
import Goods.Reservation_Trip.repository.ReviewImageRepository;
import Goods.Reservation_Trip.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class ReviewService {

    private final ReservationRepository reservationRepository;

    private final ReviewRepository reviewRepository;

    private final ReviewImageRepository reviewImageRepository;

    private final ImageManager imageManager;

    public ReviewResponseDto getReviewCreatingPage(Long reservationId,Long memberId) {

           Optional<Reservation> optionalReservation = reservationRepository.findById(reservationId);
           if(optionalReservation.isEmpty()) return null;

           Reservation reservation = optionalReservation.get();

           //다른 사람의 예약 리뷰 작성을 시도할 경우
           if(!memberId.equals(reservation.getMember().getId())){
               throw new RuntimeException("잘못된 접근");
           }

           return ReviewResponseDto.builder()
                   .packageMainImage(reservation.getAPackage().getMainImage().getImageFullName())
                   .packageName(reservation.getAPackage().getPackageName())
                   .build();
    }

    public void registerReview(ReviewDto reviewDto,Long memberId){

        Optional<Reservation> optionalReservation = reservationRepository.findById(reviewDto.getReservationId());
        if(optionalReservation.isEmpty()) throw new RuntimeException("잘못된 접근");

        Reservation reservation = optionalReservation.get();

        //다른 사람의 예약 리뷰 작성을 시도할 경우
        if(!memberId.equals(reservation.getMember().getId())){
            throw new RuntimeException("잘못된 접근");
        }

        Review review = new Review();
        review.setAPackage(reservation.getAPackage());
        review.setMember(reservation.getMember());
        review.setContent(reviewDto.getContent());
        review.setRating(reviewDto.getRating());
        review.setReservation(reservation);

        reviewRepository.save(review);

        if(!(reviewDto.getReviewImages().size()==1 && reviewDto.getReviewImages().get(0).getOriginalFilename().equals(""))) {
            //리뷰 이미지가 1장 이상일 때
            for (MultipartFile multipartFile : reviewDto.getReviewImages()) {
                UUID uuid = imageManager.save(multipartFile);

                ReviewImage reviewImage = ReviewImage.builder()
                        .originalName(multipartFile.getOriginalFilename())
                        .fileExtension(imageManager.getExtensionOf(multipartFile))
                        .uuid(uuid)
                        .review(review)
                        .build();

                reviewImageRepository.save(reviewImage);
            }
        }

    }
}
