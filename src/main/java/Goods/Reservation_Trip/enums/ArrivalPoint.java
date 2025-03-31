package Goods.Reservation_Trip.enums;

import lombok.Getter;

@Getter
public enum ArrivalPoint implements BaseEnum{
    GIMPO("김포국제공항"),
    NARITA("나리타국제공항");

    private final String name;

    ArrivalPoint(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    public static ArrivalPoint fromName(String name) {
        for (ArrivalPoint arrivalPoint : ArrivalPoint.values()) {
            if (arrivalPoint.getName().equals(name)) {
                return arrivalPoint;
            }
        }
        throw new IllegalArgumentException("존재하지 않는 항공" + name);
    }
}
