package Goods.Reservation_Trip.dto.HanDto;

import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KakaoPay {

    //카카오 페이 요청 고유 번호 - 승인/취소가 구분된 결제번호
    private String aid;
    //카카오 페이 결제 고유 번호 - 승인/취소가 동일한 결제번호
    private String tid;
    //총 결제 가격
    private int amount;
    //결제 승인 시각
    private LocalDateTime approvedAt;
    //결제 수단, CARD 또는 MONEY 중 하나
    private String payType;

    //----결제 수단이 카드일시----

    //카카오페이 매입사명
    private String purCorp;
    //카카오페이 매입사 코드
    private String purCorpCode;
    //카카오페이 발급사명
    private String issuerCorp;
    //카드사 승인 번호
    private String approved;

}
