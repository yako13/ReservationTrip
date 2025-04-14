package Goods.Reservation_Trip.service.HanService;

import Goods.Reservation_Trip.dto.HanDto.PackReviewDto;
import Goods.Reservation_Trip.entity.Package;
import Goods.Reservation_Trip.entity.Review;
import Goods.Reservation_Trip.repository.ReviewRepository;
import Goods.Reservation_Trip.repository.aPackage.PackageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class HanReviewService {

    private final ReviewRepository reviewRepository;

    private final PackageRepository packageRepository;

    //패키지 상세 페이지에 리뷰 목록 나타내는 서비스
    public PackReviewDto packReviewGo(Long packagePk) {

        Package packageEntity = packageRepository.findById(packagePk).orElse(null);

        if (packageEntity == null) {

            log.error("패키지 엔티티가 없습니다");
            return null;
        }

        List<Review> reviewList = packageEntity.getReviewList();

        boolean reviewNo = false;

        int reviewCnt = 0;

        //리뷰가 비어있거나 없을경우 reviewNo를 true로
        if (reviewList == null || reviewList.isEmpty()) {

            reviewNo = true;

        }else {

            reviewCnt  = reviewList.size();

        }

        PackReviewDto packReviewDto = PackReviewDto.builder()
                //리뷰 리스트
                .reviewList(reviewList)
                //리뷰 있는지 여부 T = 리뷰 없음 F = 리뷰 있음
                .reviewNo(reviewNo)
                //리뷰수
                .reviewCnt(reviewCnt)
                .build();


        return packReviewDto;

    }


}
