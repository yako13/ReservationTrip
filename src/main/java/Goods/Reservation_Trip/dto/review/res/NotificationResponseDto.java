package Goods.Reservation_Trip.dto.review.res;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationResponseDto {

    private Long notificationPK;

    private String content;

    private boolean read;

    private String memberName;

    private String createdAt;

    private Long reservationPK;
}
