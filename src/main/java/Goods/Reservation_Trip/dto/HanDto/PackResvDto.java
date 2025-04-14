package Goods.Reservation_Trip.dto.HanDto;


import lombok.*;

import java.time.LocalDate;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PackResvDto {

    //성인 인원수
    private int adultCnt;

    //아동 인원수
    private int childCnt;

    //유아 인원수
    private int babyCnt;

    //출발일
    private LocalDate tripStart;

    //패키지 pk
    private Long packagePk;



}
