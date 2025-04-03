package Goods.Reservation_Trip.dto.reservation.req;

import lombok.*;

import java.time.LocalDate;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class MemberReservationSearchDto {

    //검색 기준
    private String searchSelect;

    //조회 시작일
    private String startDate;

    //조회 마감일
    private String endDate;
}
