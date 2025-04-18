package Goods.Reservation_Trip.dto.review.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationApiDto {

    private Long totalNonReadCount;

    private List<NotificationResponseDto> notificationResponseDtoList;
}
