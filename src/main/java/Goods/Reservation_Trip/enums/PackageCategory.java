package Goods.Reservation_Trip.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Getter
public enum PackageCategory implements BaseEnum{
    // 대분류
    JAPAN("일본", null),
    CENTRAL_ASIA("중국 / 홍콩 / 몽골 / 중앙아시아", null),
    SOUTHEAST_ASIA("동남아 / 대만 / 서남아", null),
    EUROPA("유럽 / 아프리카", null),
    LATIN_AMERICA("미주 / 하와이 / 캐나다 / 중남미", null),
    OCEANIA("괌/ 사이판 / 뉴질랜드 / 호주", null),

    // 중분류
    KANSAI("간사이(관서)", JAPAN),
    KANTO("간토(관동)", JAPAN),
    WESTERN_EUROPA("서유럽", EUROPA),
    BEIJING("북경", CENTRAL_ASIA),

    // 소분류
    TOKYO("도쿄", KANTO),
    OSAKA("오사카", KANSAI),
    ENGLAND("영국", WESTERN_EUROPA);



    private final String name;

    private final PackageCategory parent;

    PackageCategory(String name, PackageCategory parent) {
        this.name = name;
        this.parent = parent;
    }

    public static List<PackageCategory> getSubCategories(PackageCategory parentCategory) {
        return Arrays.stream(PackageCategory.values())
                .filter(category -> Objects.equals(category.getParent(), parentCategory))
                .collect(Collectors.toList());
    }

    @Override
    public String getName() {
        return name;
    }

    public static PackageCategory fromName(String name) {
        for (PackageCategory packageCategory : PackageCategory.values()) {
            if (packageCategory.getName().equals(name)) {
                return packageCategory;
            }
        }
        throw new IllegalArgumentException("존재하지않는 카테고리" + name);
    }
}
