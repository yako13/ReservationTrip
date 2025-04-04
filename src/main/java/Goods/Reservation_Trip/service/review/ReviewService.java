package Goods.Reservation_Trip.service.review;

import Goods.Reservation_Trip.config.ImageManager;
import Goods.Reservation_Trip.dto.reservation.res.ReservationDetailsResponseDto;
import Goods.Reservation_Trip.dto.review.req.ReviewDto;
import Goods.Reservation_Trip.dto.review.res.ReviewResponseDto;
import Goods.Reservation_Trip.entity.Reservation;
import Goods.Reservation_Trip.entity.Review;
import Goods.Reservation_Trip.entity.ReviewImage;
import Goods.Reservation_Trip.repository.ReservationRepository;
import Goods.Reservation_Trip.repository.ReviewImageRepository;
import Goods.Reservation_Trip.repository.ReviewRepository;
import Goods.Reservation_Trip.util.FileStorageService;
import Goods.Reservation_Trip.util.Formatter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
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

    private final FileStorageService fileStorageService;

    /**
     * 리뷰 페이지
     */
    public ReviewResponseDto getReviewCreatingPage(Long reservationId) {

        Optional<Reservation> optionalReservation = reservationRepository.findById(reservationId);
        if (optionalReservation.isEmpty()) return null;

        Reservation reservation = optionalReservation.get();

        //리뷰는 1개만 가능
        if (!reservation.getReviewList().isEmpty()) return null;

        return ReviewResponseDto.builder()
                .packageMainImage(imageManager.createImageUrl(reservation.getAPackage().getMainImage().getImageFullName()))
                .packageName(reservation.getAPackage().getPackageName())
                .build();
    }

    /**
     * 리뷰 저장
     */
    public void registerReview(ReviewDto reviewDto, Long memberId) {

        Optional<Reservation> optionalReservation = reservationRepository.findById(reviewDto.getReservationId());
        if (optionalReservation.isEmpty()) throw new RuntimeException("잘못된 접근");

        Reservation reservation = optionalReservation.get();

        //다른 사람의 예약 리뷰 작성을 시도할 경우
        if (!memberId.equals(reservation.getMember().getId())) {
            throw new RuntimeException("잘못된 접근");
        }

        //리뷰는 1개만 가능
        if (!reservation.getReviewList().isEmpty()) throw new RuntimeException("리뷰는 한개만 등록가능");

        Review review = new Review();
        review.setAPackage(reservation.getAPackage());
        review.setMember(reservation.getMember());
        review.setContent(reviewDto.getContent());
        review.setRating(reviewDto.getRating());
        review.setReservation(reservation);

        reviewRepository.save(review);

        if (!(reviewDto.getReviewImages().size() == 1 && reviewDto.getReviewImages().get(0).getOriginalFilename().equals(""))) {
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

    /**
     * 리뷰 수정 페이지
     */
    public ReviewResponseDto getReviewEditPage(Long reviewId) {

        Optional<Review> optionalReview = reviewRepository.findById(reviewId);

        if (optionalReview.isEmpty()) throw new RuntimeException("잘못된 접근");

        Review review = optionalReview.get();

        List<String> images = new ArrayList<>();

        for (ReviewImage reviewImage : review.getReviewImageList()) {
            images.add(imageManager.createImageUrl(reviewImage.getImageFullName()));
        }

        return ReviewResponseDto.builder()
                .packageMainImage(imageManager.createImageUrl(review.getAPackage().getMainImage().getImageFullName()))
                .packageName(review.getAPackage().getPackageName())
                .content(review.getContent())
                .rating(review.getRating())
                .imagesURL(images)
                .reviewId(reviewId)
                .build();
    }

    /**
     * 리뷰 수정
     */
    public void editReview(ReviewDto reviewDto, Long memberId, String deletedImages) {
        Optional<Review> optionalReview = reviewRepository.findById(reviewDto.getReviewId());

        if (optionalReview.isEmpty()) throw new RuntimeException("잘못된 접근");

        Review review = optionalReview.get();

        //다른 사람의 예약 리뷰 수정을 시도할 경우
        if (!review.getMember().getId().equals(memberId)) throw new RuntimeException("잘못된 접근");

        review.setContent(reviewDto.getContent());
        review.setRating(reviewDto.getRating());

        reviewRepository.save(review);

        List<ReviewImage> reviewImageList = review.getReviewImageList();

        // 삭제 요청이 있으면 이미지 삭제 처리
        if (deletedImages != null && !deletedImages.trim().isEmpty()) {
            String[] deletedImageList = deletedImages.split(",");

            for (String deletedImage : deletedImageList) {
                if (deletedImage.isEmpty()) continue;

                if (deletedImage.startsWith("subImage")) {
                    try {
                        int index = Integer.parseInt(deletedImage.replace("subImage", ""));
                        if (index < reviewImageList.size()) {
                            ReviewImage imageToDelete = reviewImageList.get(index);
                            fileStorageService.deleteFile(imageToDelete.getImageFullName());

                            reviewImageRepository.delete(imageToDelete); // DB에서 삭제
                            review.getReviewImageList().remove(imageToDelete); // 리스트에서 제거
                        }
                    } catch (NumberFormatException e) {
                        System.err.println("Invalid subImage index: " + deletedImage);
                    }
                }
            }
        }
        //새로운 이미지 저장
        if (!(reviewDto.getReviewImages().size() == 1 && reviewDto.getReviewImages().get(0).getOriginalFilename().equals(""))) {

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

    /**
     * 리뷰 작성 가능한 예약 불러오기
     */
    public List<ReservationDetailsResponseDto> getReviewAblePage(Long memberId) {
        //리뷰 리스트가 0인 예약만 찾아옴
        List<Reservation> reservationList = reservationRepository.findByReviewListIsNull();

        if (reservationList.isEmpty()) return null;

        List<ReservationDetailsResponseDto> responseDtos = new ArrayList<>();

        for (Reservation reservation : reservationList) {
            ReservationDetailsResponseDto reservationDetailsResponseDto = ReservationDetailsResponseDto.builder()
                    .reservationPK(reservation.getId())
                    .tag(Formatter.getTag(reservation.getAPackage().getPackageName()))
                    .packageName(Formatter.getPackageNameWithoutTag(reservation.getAPackage().getPackageName()))
                    .startDate(reservation.getStartDate().toString())
                    .endDate(reservation.getEndDate().toString())
                    .packagePK(reservation.getAPackage().getId())
                    .mainImage(imageManager.createImageUrl(reservation.getAPackage().getMainImage().getImageFullName()))
                    .build();

            responseDtos.add(reservationDetailsResponseDto);
        }
        return responseDtos;
    }
}
