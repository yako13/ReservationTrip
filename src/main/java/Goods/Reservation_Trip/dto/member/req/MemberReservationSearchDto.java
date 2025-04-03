package Goods.Reservation_Trip.dto.member.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberReservationSearchDto {

    //검색 기준
    private String searchSelect;

    //조회 시작일
    private String startDate;

    //조회 마감일
    private String endDate;
    
    //조회 결과 개수
    private int count;

}
