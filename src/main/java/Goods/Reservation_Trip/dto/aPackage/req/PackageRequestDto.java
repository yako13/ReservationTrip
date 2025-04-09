package Goods.Reservation_Trip.dto.aPackage.req;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PackageRequestDto {

    private Long id;
    /**
     * 패키지명
     */
    private String packageName;

    /**
     * 최대 에약 가능 인원
     */
    private int maximumMember;

    /**
     * 출발에 필요한 최소 예약 인원
     */
    private int minimumRequired;

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
    private MultipartFile mainImage;

    /**
     * 서브 이미지
     */
    private List<MultipartFile> subImage;

    /**
     * 일정 설명 이미지
     */
    private List<MultipartFile> descImage;

    /**
     * 호텔명 리스트
     */
    private String hotelName;

    /**
     * 대분류 ID
     */
    private Long mainCategoryId;

    /**
     * 중분류 ID
     */
    private Long subCategoryId;

    /**
     * 소분류 ID
     */
    private Long smallCategoryId;

    /**
     * 1개의 패키지의 일정 리스트
     */
    private List<PackageScheduleRequestDto> schedules = new ArrayList<>();
}
