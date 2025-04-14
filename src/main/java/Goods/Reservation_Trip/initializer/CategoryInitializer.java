package Goods.Reservation_Trip.initializer;

import Goods.Reservation_Trip.entity.PackageCategory;
import Goods.Reservation_Trip.repository.aPackage.PackageCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Order(1)
public class CategoryInitializer implements CommandLineRunner {

    private final PackageCategoryRepository packageCategoryRepository;

    // 카테고리 생성
    @Override
    public void run(String... args) {
        // 대분류 depth:1
        PackageCategory japan = packageCategoryRepository.findByName("일본")
                .orElseGet(() -> packageCategoryRepository.save(new PackageCategory(null, "일본", null, 1)));

        PackageCategory china = packageCategoryRepository.findByName("중국")
                .orElseGet(() -> packageCategoryRepository.save(new PackageCategory(null, "중국", null, 1)));
        PackageCategory usa = packageCategoryRepository.findByName("미국")
                .orElseGet(() -> packageCategoryRepository.save(new PackageCategory(null, "미국", null, 1)));


        // 중분류 depth:2
        //일본
        PackageCategory kansai = packageCategoryRepository.findByName("간사이(관서)")
                .orElseGet(() -> packageCategoryRepository.save(new PackageCategory(null, "간사이(관서)", japan, 2)));
        PackageCategory kanto = packageCategoryRepository.findByName("간토(관동)")
                .orElseGet(() -> packageCategoryRepository.save(new PackageCategory(null, "간토(관동)", japan, 2)));
        PackageCategory hokkaido = packageCategoryRepository.findByName("홋카이도")
                .orElseGet(() -> packageCategoryRepository.save(new PackageCategory(null, "홋카이도", japan, 2)));
        PackageCategory kyushu = packageCategoryRepository.findByName("큐슈")
                .orElseGet(() -> packageCategoryRepository.save(new PackageCategory(null, "큐슈", japan, 2)));

        //중국
        PackageCategory northChina = packageCategoryRepository.findByName("화북")
                .orElseGet(() -> packageCategoryRepository.save(new PackageCategory(null, "화북", china, 2)));
        PackageCategory eastChina = packageCategoryRepository.findByName("화동")
                .orElseGet(() -> packageCategoryRepository.save(new PackageCategory(null, "화동", china, 2)));
        PackageCategory northeastChina = packageCategoryRepository.findByName("동북")
                .orElseGet(() -> packageCategoryRepository.save(new PackageCategory(null, "동북", china, 2)));

        //미국
        PackageCategory westRegion = packageCategoryRepository.findByName("서부")
                .orElseGet(() -> packageCategoryRepository.save(
                        new PackageCategory(null, "서부", usa, 2)));

        PackageCategory eastRegion = packageCategoryRepository.findByName("동부")
                .orElseGet(() -> packageCategoryRepository.save(
                        new PackageCategory(null, "동부", usa, 2)));

        PackageCategory midwestRegion = packageCategoryRepository.findByName("중부")
                .orElseGet(() -> packageCategoryRepository.save(
                        new PackageCategory(null, "중부", usa, 2)));

        PackageCategory southRegion = packageCategoryRepository.findByName("남부")
                .orElseGet(() -> packageCategoryRepository.save(
                        new PackageCategory(null, "남부", usa, 2)));

        PackageCategory alaska = packageCategoryRepository.findByName("알래스카")
                .orElseGet(() -> packageCategoryRepository.save(
                        new PackageCategory(null, "알래스카", usa, 2)));

        PackageCategory hawaii = packageCategoryRepository.findByName("하와이")
                .orElseGet(() -> packageCategoryRepository.save(
                        new PackageCategory(null, "하와이", usa, 2)));


        // 소분류 depth:3
        if (!packageCategoryRepository.existsByName("오사카")) {
            PackageCategory osaka = new PackageCategory(null, "오사카", kansai, 3);
            packageCategoryRepository.save(osaka);
        }
        if (!packageCategoryRepository.existsByName("교토")) {
            PackageCategory kyoto = new PackageCategory(null, "교토", kansai, 3);
            packageCategoryRepository.save(kyoto);
        }
        if (!packageCategoryRepository.existsByName("고베")) {
            PackageCategory kobe = new PackageCategory(null, "고베", kansai, 3);
            packageCategoryRepository.save(kobe);
        }
        if (!packageCategoryRepository.existsByName("와카야마")) {
            PackageCategory wakayama = new PackageCategory(null, "와카야마", kansai, 3);
            packageCategoryRepository.save(wakayama);
        }
        if (!packageCategoryRepository.existsByName("도쿄")) {
            PackageCategory tokyo = new PackageCategory(null, "도쿄", kanto, 3);
            packageCategoryRepository.save(tokyo);
        }
        if (!packageCategoryRepository.existsByName("시즈오카")) {
            PackageCategory Shizuoka = new PackageCategory(null, "시즈오카", kanto, 3);
            packageCategoryRepository.save(Shizuoka);
        }
        if (!packageCategoryRepository.existsByName("하코네")) {
            PackageCategory Hakone = new PackageCategory(null, "하코네", kanto, 3);
            packageCategoryRepository.save(Hakone);
        }
        if (!packageCategoryRepository.existsByName("요코하마")) {
            PackageCategory Yokohama = new PackageCategory(null, "요코하마", kanto, 3);
            packageCategoryRepository.save(Yokohama);
        }
        if (!packageCategoryRepository.existsByName("삿포로")) {
            PackageCategory sapporo = new PackageCategory(null, "삿포로", hokkaido, 3);
            packageCategoryRepository.save(sapporo);
        }
        if (!packageCategoryRepository.existsByName("후라노")) {
            PackageCategory furano = new PackageCategory(null, "후라노", hokkaido, 3);
            packageCategoryRepository.save(furano);
        }
        if (!packageCategoryRepository.existsByName("오타루")) {
            PackageCategory otaru = new PackageCategory(null, "오타루", hokkaido, 3);
            packageCategoryRepository.save(otaru);
        }
        if (!packageCategoryRepository.existsByName("후쿠오카")) {
            PackageCategory fukuoka = new PackageCategory(null, "후쿠오카", kyushu, 3);
            packageCategoryRepository.save(fukuoka);
        }
        if (!packageCategoryRepository.existsByName("나가사키")) {
            PackageCategory nagasaki = new PackageCategory(null, "나가사키", kyushu, 3);
            packageCategoryRepository.save(nagasaki);
        }
        if (!packageCategoryRepository.existsByName("뱃푸")) {
            PackageCategory beppu = new PackageCategory(null, "뱃푸", kyushu, 3);
            packageCategoryRepository.save(beppu);
        }


        if (!packageCategoryRepository.existsByName("베이징")) {
            PackageCategory beijing = new PackageCategory(null, "베이징", northChina, 3);
            packageCategoryRepository.save(beijing);
        }
        if (!packageCategoryRepository.existsByName("텐진")) {
            PackageCategory tianjin = new PackageCategory(null, "텐진", northChina, 3);
            packageCategoryRepository.save(tianjin);
        }

        if (!packageCategoryRepository.existsByName("상하이")) {
            PackageCategory shanghai = new PackageCategory(null, "상하이", eastChina, 3);
            packageCategoryRepository.save(shanghai);
        }
        if (!packageCategoryRepository.existsByName("항저우")) {
            PackageCategory hangzhou = new PackageCategory(null, "항저우", eastChina, 3);
            packageCategoryRepository.save(hangzhou);
        }

        if (!packageCategoryRepository.existsByName("하얼빈")) {
            PackageCategory harbin = new PackageCategory(null, "하얼빈", northeastChina, 3);
            packageCategoryRepository.save(harbin);
        }

        if (!packageCategoryRepository.existsByName("로스앤젤레스")) {
            PackageCategory losAngeles = new PackageCategory(null, "로스앤젤레스", westRegion, 3);
            packageCategoryRepository.save(losAngeles);
        }

        if (!packageCategoryRepository.existsByName("라스베가스")) {
            PackageCategory lasVegas = new PackageCategory(null, "라스베가스", westRegion, 3);
            packageCategoryRepository.save(lasVegas);
        }

        if (!packageCategoryRepository.existsByName("샌프란시스코")) {
            PackageCategory sanFrancisco = new PackageCategory(null, "샌프란시스코", westRegion, 3);
            packageCategoryRepository.save(sanFrancisco);
        }

        if (!packageCategoryRepository.existsByName("시애틀")) {
            PackageCategory seattle = new PackageCategory(null, "시애틀", westRegion, 3);
            packageCategoryRepository.save(seattle);
        }

        // East Region 소분류
        if (!packageCategoryRepository.existsByName("뉴욕")) {
            PackageCategory newYork = new PackageCategory(null, "뉴욕", eastRegion, 3);
            packageCategoryRepository.save(newYork);
        }

        if (!packageCategoryRepository.existsByName("워싱턴 D.C.")) {
            PackageCategory washingtonDC = new PackageCategory(null, "워싱턴 D.C.", eastRegion, 3);
            packageCategoryRepository.save(washingtonDC);
        }

        if (!packageCategoryRepository.existsByName("보스턴")) {
            PackageCategory boston = new PackageCategory(null, "보스턴", eastRegion, 3);
            packageCategoryRepository.save(boston);
        }

        // Midwest Region 소분류
        if (!packageCategoryRepository.existsByName("시카고")) {
            PackageCategory chicago = new PackageCategory(null, "시카고", midwestRegion, 3);
            packageCategoryRepository.save(chicago);
        }

        // South Region 소분류
        if (!packageCategoryRepository.existsByName("댈러스")) {
            PackageCategory dallas = new PackageCategory(null, "댈러스", southRegion, 3);
            packageCategoryRepository.save(dallas);
        }

        if (!packageCategoryRepository.existsByName("마이애미")) {
            PackageCategory miami = new PackageCategory(null, "마이애미", southRegion, 3);
            packageCategoryRepository.save(miami);
        }

        // Alaska 소분류
        if (!packageCategoryRepository.existsByName("앵커리지")) {
            PackageCategory anchorage = new PackageCategory(null, "앵커리지", alaska, 3);
            packageCategoryRepository.save(anchorage);
        }

        // Hawaii 소분류
        if (!packageCategoryRepository.existsByName("호놀룰루")) {
            PackageCategory honolulu = new PackageCategory(null, "호놀룰루", hawaii, 3);
            packageCategoryRepository.save(honolulu);
        }
    }
}
