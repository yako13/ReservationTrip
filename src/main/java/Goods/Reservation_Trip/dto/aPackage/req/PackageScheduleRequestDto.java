package Goods.Reservation_Trip.dto.aPackage.req;

import Goods.Reservation_Trip.enums.PackageStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PackageScheduleRequestDto {

    private Long id;

    /**
     * 최대 에약 가능 인원
     */
    private int maximumMember;

    /**
     * 출발에 필요한 최소 예약 인원
     */
    private int minimumRequired;

    /**
     * 일정을 예약한 인원수
     */
    private int reservedMemberCount;

    /**
     * 예약 가능 상태
     */
    @Enumerated(EnumType.STRING)
    private PackageStatus packageStatus;

    /**
     * 출국 일자
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate departureDateOut;

    /**
     * 출국 도착 일자
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate arrivalDateOut;

    /**
     * 귀국 일자
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate departureDateReturn;

    /**
     * 귀국 도착 일자
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate arrivalDateReturn;

    @Getter
    @NotNull
    private PackageScheduleDetailsRequestDto details;

}
