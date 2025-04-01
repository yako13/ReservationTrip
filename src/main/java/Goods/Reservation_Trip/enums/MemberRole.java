package Goods.Reservation_Trip.enums;

import lombok.Getter;

@Getter
public enum MemberRole implements BaseEnum{
    ADMIN("관리자"),
    MEMBER("일반"),
    CANCELLATION("탈퇴회원");

    private final String name;

    MemberRole(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    public static MemberRole fromName(String name) {
        for (MemberRole role : MemberRole.values()) {
            if (role.getName().equals(name)) {
                return role;
            }
        }
        throw new IllegalArgumentException("존재하지 않는 역할: " + name);
    }
}
