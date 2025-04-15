package Goods.Reservation_Trip.service.review;

import Goods.Reservation_Trip.config.ImageManager;
import Goods.Reservation_Trip.dto.reservation.res.ReservationDetailsResponseDto;
import Goods.Reservation_Trip.dto.review.req.ReviewDto;
import Goods.Reservation_Trip.dto.review.res.ReviewResponseDto;
import Goods.Reservation_Trip.entity.Package;
import Goods.Reservation_Trip.entity.Reservation;
import Goods.Reservation_Trip.entity.Review;
import Goods.Reservation_Trip.entity.ReviewImage;
import Goods.Reservation_Trip.repository.ReservationRepository;
import Goods.Reservation_Trip.repository.ReviewImageRepository;
import Goods.Reservation_Trip.repository.ReviewRepository;
import Goods.Reservation_Trip.repository.aPackage.PackageRepository;
import Goods.Reservation_Trip.util.FileStorageService;
import Goods.Reservation_Trip.util.Formatter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
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
        if (reservation.getReview() != null) return null;

        return ReviewResponseDto.builder()
                .packagePK(reservation.getAPackage().getId())
                .packageMainImage(imageManager.createImageUrl(reservation.getAPackage().getMainImage().getImageFullName()))
                .packageName(reservation.getAPackage().getPackageName())
                .build();
    }

    /**
     * 평점 등록
     */
    public double setAverageRating(Package aPackage, int reviewRating) {

        int totalRating = 0;

        for (Review review : aPackage.getReviewList()) {
            totalRating += review.getRating();
        }
        //첫째자리까지 반올림
        return Math.round(((double) (totalRating + reviewRating) / (aPackage.getReviewList().size() + 1)) * 10) / 10.0;

    }

    /**
     * 평점 수정
     */
    public double editAverageRating(Package aPackage, int reviewRating, int newReviewRating) {

        int totalRating = 0;

        for (Review review : aPackage.getReviewList()) {
            totalRating += review.getRating();
        }

        totalRating = totalRating - reviewRating + newReviewRating; // 총 평점 = 기존 총 평점 - 기존 리뷰 평점 + 새 리뷰 평점

        //첫째자리까지 반올림
        return Math.round(((double) (totalRating) / (aPackage.getReviewList().size())) * 10) / 10.0;

    }

    /**
     * 평점 삭제
     */
    public double deleteAverageRating(Package aPackage, int reviewRating) {

        int totalRating = 0;

        for (Review review : aPackage.getReviewList()) {
            totalRating += review.getRating();
        }

        //첫째자리까지 반올림
        return Math.round(((double) (totalRating - reviewRating) / (aPackage.getReviewList().size() - 1)) * 10) / 10.0;

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
        Review review = new Review();

        //리뷰는 1개만 가능
        if (reservation.getReview() != null) {
            throw new RuntimeException("리뷰는 한개만 등록가능");
        }

        Package aPackage = reservation.getAPackage();

        //평균 평점 등록
        double averageRating = setAverageRating(aPackage, reviewDto.getRating());
        aPackage.setAverageRating(averageRating);

        review.setAPackage(aPackage);
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

        return ReviewResponseDto.builder()
                .packageMainImage(imageManager.createImageUrl(review.getAPackage().getMainImage().getImageFullName()))
                .packagePK(review.getAPackage().getId())
                .packageName(review.getAPackage().getPackageName())
                .content(review.getContent())
                .rating(review.getRating())
                .reviewImageList(review.getReviewImageList())
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

        Package aPackage = review.getAPackage();

        //평균 평점 수정
        double averageRating = editAverageRating(aPackage, review.getRating(), reviewDto.getRating());
        aPackage.setAverageRating(averageRating);

        review.setAPackage(aPackage);
        review.setContent(reviewDto.getContent());
        review.setRating(reviewDto.getRating());

        reviewRepository.save(review);

        List<ReviewImage> reviewImageList = review.getReviewImageList();

        // 삭제 요청이 있으면 이미지 삭제 처리
        if (deletedImages != null && !deletedImages.trim().isEmpty()) {
            String[] deletedImageList = deletedImages.split(",");

            for (String deletedImage : deletedImageList) {
                if (deletedImage.isEmpty()) continue;

                if (deletedImage.startsWith("subImage|")) {
                    String imageName = deletedImage.replace("subImage|", "");

                    Optional<ReviewImage> imageToDeleteOpt = review.getReviewImageList().stream()
                            .filter(img -> img.getImageFullName().equals(imageName))
                            .findFirst();

                    if (imageToDeleteOpt.isPresent()) {
                        ReviewImage imageToDelete = imageToDeleteOpt.get();
                        fileStorageService.deleteFile(imageToDelete.getImageFullName());
                        reviewImageRepository.delete(imageToDelete);
                        review.getReviewImageList().remove(imageToDelete);
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
        //리뷰 리스트가 0이며, 여행 마지막날이 현재 날짜를 지나고, ID 기준 내림차순으로 정렬
        List<Reservation> reservationList = reservationRepository.findByEndDateBeforeAndReviewIsNullAndMemberIdOrderByIdDesc(LocalDate.now(), memberId);

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

    /**
     * 리뷰 리스트
     */
    public List<ReviewResponseDto> getReviewList(Long memberId) {
        List<Review> reviewList = reviewRepository.findByMemberIdOrderByCreatedAtDesc(memberId);

        List<ReviewResponseDto> responseDtoList = new ArrayList<>();

        for (Review review : reviewList) {
            boolean modified = true;
            //수정됐는지 안됐는지만 체크
            //수정 날짜와 생성 날짜가 같으면 수정하지 않았음
            if (review.getCreatedAt().equals(review.getModifiedAt())) {
                modified = false;
            }

            List<String> images = new ArrayList<>();

            //리뷰 이미지 가져오기
            for (ReviewImage reviewImage : review.getReviewImageList()) {
                images.add(imageManager.createImageUrl(reviewImage.getImageFullName()));
            }

            ReviewResponseDto reviewResponseDto = ReviewResponseDto.builder()
                    .packagePK(review.getAPackage().getId())
                    .reviewId(review.getId())
                    .packageMainImage(imageManager.createImageUrl(review.getAPackage().getMainImage().getImageFullName()))
                    .packageName(review.getAPackage().getPackageName())
                    .content(review.getContent())
                    .rating(review.getRating())
                    .createdAt(Formatter.getLocalDate(review.getCreatedAt()))
                    .modified(modified)
                    .imagesURL(images)
                    .build();

            responseDtoList.add(reviewResponseDto);
        }
        return responseDtoList;
    }

    /**
     * 리뷰삭제
     */
    @Transactional
    public String deleteReview(Long id, Long memberId) {
        Optional<Review> optionalReview = reviewRepository.findById(id);
        if (optionalReview.isEmpty()) return "500";

        Review review = optionalReview.get();

        //다른 사람 리뷰 삭제 불가
        if (!review.getMember().getId().equals(memberId)) return "500";

        Package aPackage = review.getAPackage();

        double averageRating = deleteAverageRating(aPackage, review.getRating());
        aPackage.setAverageRating(averageRating);

        aPackage.getReviewList().remove(review);
        Reservation reservation = review.getReservation();

        reservation.setReview(null);

        reservationRepository.save(reservation);

        review.setAPackage(null);

        reviewRepository.delete(review);


        return "1000";
    }
}
