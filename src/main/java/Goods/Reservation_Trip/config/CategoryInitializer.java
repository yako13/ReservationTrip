package Goods.Reservation_Trip.config;

import Goods.Reservation_Trip.entity.PackageCategory;
import Goods.Reservation_Trip.repository.aPackage.PackageCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CategoryInitializer implements CommandLineRunner{

    private final PackageCategoryRepository packageCategoryRepository;

    // 카테고리 생성
    @Override
    public void run(String... args) {
        if (packageCategoryRepository.count() == 0) {
            // 대분류 depth:1
            PackageCategory japan = new PackageCategory(null, "일본", null, 1);
            packageCategoryRepository.save(japan);
            PackageCategory china = new PackageCategory(null, "중국", null, 1);
            packageCategoryRepository.save(china);

            // 중분류 depth:2
            PackageCategory kansai = new PackageCategory(null, "간사이", japan, 2);
            packageCategoryRepository.save(kansai);
            PackageCategory kanto = new PackageCategory(null, "간토", japan, 2);
            packageCategoryRepository.save(kanto);

            // 소분류 depth:3
            PackageCategory osaka = new PackageCategory(null, "오사카", kansai, 3);
            packageCategoryRepository.save(osaka);
            PackageCategory tokyo = new PackageCategory(null, "도쿄", kansai, 3);
            packageCategoryRepository.save(tokyo);

            PackageCategory osaka1 = new PackageCategory(null, "오사카1", kansai, 3);
            packageCategoryRepository.save(osaka1);
            PackageCategory tokyo1 = new PackageCategory(null, "도쿄1", kansai, 3);
            packageCategoryRepository.save(tokyo1);
        }
    }
}
