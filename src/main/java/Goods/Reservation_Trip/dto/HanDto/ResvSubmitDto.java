package Goods.Reservation_Trip.dto.HanDto;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResvSubmitDto {

    //패키지 pk
    private Long packagePk;

    //여행 출발일
    private LocalDate tripStart;

    //여행 종료일
    private LocalDate tripEnd;

    // 여행자 정보 리스트

    //성인
    private List<TravelerDto> adult;
    //아동
    private List<TravelerDto> child;
    //유아
    private List<TravelerDto> baby;

    // 카카오페이 결제 응답 값

    //요청 고유 번호 - 승인/취소가 구분된 결제번호
    private String aid;
    //요청 고유 번호 - 승인/취소가 구분된 결제번호
    private String tid;
    //전체 결제 금액 (카카오 페이로 결제한것)
    private int amount;
    //결제 수단, CARD 또는 MONEY 중 하나
    private String payType;
    //결제 승인 시각
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime approvedAt;

    //카드 결제일 경우만

    //카카오페이 매입사명
    private String purCorp;
    //카카오페이 매입사 코드
    private String purCorpCode;
    //카카오페이 발급사명
    private String issuerCorp;
    //카드사 승인번호
    private String approved;




}
