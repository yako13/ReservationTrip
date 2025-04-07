package Goods.Reservation_Trip.config;

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


        // 중분류 depth:2
        PackageCategory kansai = packageCategoryRepository.findByName("간사이(관서)")
                .orElseGet(() -> packageCategoryRepository.save(new PackageCategory(null, "간사이(관서)", japan, 2)));
        PackageCategory kanto = packageCategoryRepository.findByName("간토(관동)")
                .orElseGet(() -> packageCategoryRepository.save(new PackageCategory(null, "간토(관동)", japan, 2)));


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
    }
}
