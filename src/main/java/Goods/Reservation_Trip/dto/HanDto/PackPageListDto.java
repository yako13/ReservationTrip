package Goods.Reservation_Trip.dto.HanDto;

import Goods.Reservation_Trip.entity.*;
import Goods.Reservation_Trip.entity.Package;
import Goods.Reservation_Trip.enums.PackageStatus;
import Goods.Reservation_Trip.util.Formatter;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;
import org.hibernate.annotations.Comment;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PackPageListDto {

    //상품 pk
    private Long id;
    //상품명
    private String packageName;
    //상품 평균 평점
    private double averageRating =0.;
    //상품 가격 (성인 1인 기준 fuelSurchargeIncluded 이 필드)
    private String fuelSurchargeIncluded;
    //상품 옵션
    private PackageOption packageOption;
    //상품 예약 가능 상태
    private PackageStatus packageStatus;
    //대표 이미지
    private PackageImage mainImage;
    //간략한 설명글
    private String description;
    //여행 일수
    private int period;

    //몇박 몇일
    private String tripDate;

    //--카테고리 시작--

    //대분류 카테고리
    private PackageCategory mainCategory;

    //"중분류 카테고리
    private PackageCategory subCategory;

    //소분류 카테고리
    private PackageCategory smallCategory;

    //패키지 카테고리
    private List<PackageCategory> packageCategory;

    //--카테고리 끝--


    //리뷰 수
    private int reviewCnt;



    public static PackPageListDto fromEntity(Package packageEntity) {
        return PackPageListDto.builder()
                //상품 pk
                .id(packageEntity.getId())
                //상품명
                .packageName(packageEntity.getPackageName())
                //평균 별점
                .averageRating(packageEntity.getAverageRating())
                //유류할증료 (성인 1인 기준상품 가격)
                .fuelSurchargeIncluded(Formatter.BigDecimalFormat2(packageEntity.getFuelSurchargeIncluded()))
                //패키지 옵션
                .packageOption(packageEntity.getPackageOption())
                //패키지 상태
                .packageStatus(packageEntity.getPackageStatus())
                //메인이미지
                .mainImage(packageEntity.getMainImage())
                //설명글
                .description(packageEntity.getDescription())
                //여행일수
                .period(packageEntity.getPeriod())
                //몇박 몇일
                .tripDate(Formatter.TripDate(packageEntity.getPeriod()))

                //카테고리 부분
                .mainCategory(packageEntity.getMainCategory())
                .subCategory(packageEntity.getSubCategory())
                .smallCategory(packageEntity.getSmallCategory())

                //패키지 카테고리 리스트
                .packageCategory(packageEntity.getPackageCategory())
                //리뷰수
                .reviewCnt(packageEntity.getReviewList().size())

                .build();
    }


}
