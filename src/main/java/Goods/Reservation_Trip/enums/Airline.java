package Goods.Reservation_Trip.enums;

import lombok.Getter;

@Getter
public enum Airline implements BaseEnum{
    KOREAN_AIR("대한항공"),
    ASIANA_AIR("아시아나항공"),
    AIR_SEOUL("에어서울"),
    AIR_BUSAN("에어부산"),
    TWAY_AIR("티웨이항공"),
    JEJU_AIR("제주항공");

    private final String name;

    Airline(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    public static Airline fromName(String name) {
        for (Airline airline : Airline.values()) {
            if (airline.getName().equals(name)) {
                return airline;
            }
        }
        throw new IllegalArgumentException("존재하지 않는 항공사" + name);
    }
}
