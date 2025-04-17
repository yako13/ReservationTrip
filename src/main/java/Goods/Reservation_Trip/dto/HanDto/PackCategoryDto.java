package Goods.Reservation_Trip.dto.HanDto;


import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PackCategoryDto {
    // 항공비 포함 여부
    private Boolean airfare;
    // 호텔비 포함 여부
    private Boolean hotelFee;
    // 가이드 동반 여부
    private Boolean guide;
    // 쇼핑 없음 여부
    private Boolean noShopping;


    // 출국 공항의 PK
    private Long airportId;

    //출발일 시작
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate startDate;
    //출발일 끝
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate endDate;


    // 카테고리 pk
    private Long categoryId;
    // 카테고리 이름
    private String categoryName;
    //카테고리 Depth 대 중 소 분류 기준 1=대, 2=중, 3=소
    private Integer categoryDepth;

    //정렬기준 (예약순, 평점순, 높은 가격순, 낮은 가격순)
    private String sortType;


    //여행기간
    private Integer period;

    //검색어
    private String keyword;


}
