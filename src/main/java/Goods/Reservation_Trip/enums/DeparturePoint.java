package Goods.Reservation_Trip.enums;

import lombok.Getter;

@Getter
public enum DeparturePoint implements BaseEnum{
    GIMPO("김포국제공항"),
    NARITA("나리타국제공항");

    private final String name;

    DeparturePoint(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    public static DeparturePoint fromName(String name) {
        for (DeparturePoint departurePoint : DeparturePoint.values()) {
            if (departurePoint.getName().equals(name)) {
                return departurePoint;
            }
        }
        throw new IllegalArgumentException("존재하지 않는 공항" + name);
    }
}
