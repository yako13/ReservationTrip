package Goods.Reservation_Trip.enums;

import lombok.Getter;

@Getter
public enum PackageStatus implements BaseEnum{
    AVAILABLE("예약가능"),
    FULL("예약매진"),
    CLOSED("예약불가"),
    CANCEL("여행취소"),
    EXPIRED("여행출발");

    private final String name;

    PackageStatus(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    public static PackageStatus fromName(String name) {
        for (PackageStatus packageStatus : PackageStatus.values()) {
            if (packageStatus.getName().equals(name)) {
                return packageStatus;
            }
        }
        throw new IllegalArgumentException(("존재하지 않는 상태" + name));
    }
}
