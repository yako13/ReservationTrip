package Goods.Reservation_Trip.dto.aPackage.res;

import Goods.Reservation_Trip.dto.aPackage.req.CategoryDto;
import Goods.Reservation_Trip.entity.PackageImage;
import Goods.Reservation_Trip.enums.PackageStatus;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PackageEditResponseDto {

    private Long id;
    /**
     * 패키지명
     */
    private String packageName;

    /**
     * 성인 1인당 금액
     */
    private BigDecimal adultPrice;

    /**
     * 아동 1인당 금액
     */
    private BigDecimal childPrice;

    /**
     * 유아 1인당 금액
     */
    private BigDecimal babyPrice;

    /**
     * 유류할증료
     */
    private BigDecimal fuelSurcharge;

    /**
     * 유류할증료 포함 금액
     */
    private BigDecimal fuelSurchargeIncluded;

    /**
     * 간단 설명글
     */
    private String description;

    /**
     * 여행 일수
     */
    private int period;

    /**
     * 메인 이미지
     */
    private PackageImage mainImage;

    /**
     * 서브 이미지
     */
    private List<PackageImage> subImage;

    /**
     * 일정 설명 이미지
     */
    private List<PackageImage> descImage;

    /**
     * 호텔명 리스트
     */
    private String hotelName;

    /**
     * 대분류 ID
     */
    private CategoryDto mainCategoryId;

    /**
     * 중분류 ID
     */
    private CategoryDto subCategoryId;

    /**
     * 소분류 ID
     */
    private CategoryDto smallCategoryId;

    /**
     * 예약 상태
     */
    private PackageStatus packageStatus;

    /**
     * 1개의 패키지의 일정 리스트
     */
    private List<PackageScheduleEditResponseDto> schedules = new ArrayList<>();

    /**
     * 1개의 패키지의 옵션
     */
    private List<PackageOptionEditResponseDto> options = new ArrayList<>();
}
