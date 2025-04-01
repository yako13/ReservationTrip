package Goods.Reservation_Trip.enums;

public enum AgeGroup implements BaseEnum{

    CHILD("어린이"),
    INFANT("유아"),
    ADULT("성인");

    private final String name;

    AgeGroup(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    public static AgeGroup fromName(String name) {
        for (AgeGroup ageGroup : AgeGroup.values()) {
            if (ageGroup.getName().equals(name)) {
                return ageGroup;
            }
        }
        throw new IllegalArgumentException("존재하지 않는 연령" + name);
    }

}
