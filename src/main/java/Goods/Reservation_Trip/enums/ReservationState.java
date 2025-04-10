package Goods.Reservation_Trip.enums;

import lombok.Getter;

@Getter
public enum ReservationState implements BaseEnum  {
    CANCEL("예약취소"),
    WAIT("예약보류"),
    REQUEST("예약취소요청"),
    CONFIRM("예약완료");

    private final String name;

    ReservationState(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    public static ReservationState fromName(String name) {
        for (ReservationState reservationState : ReservationState.values()) {
            if (reservationState.getName().equals(name)) {
                return reservationState;
            }
        }
        throw new IllegalArgumentException("존재하지 않는 예약상태" + name);
    }
}
