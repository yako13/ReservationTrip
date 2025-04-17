package Goods.Reservation_Trip.service.aPackage;

import Goods.Reservation_Trip.entity.PackageCategory;
import Goods.Reservation_Trip.repository.aPackage.PackageCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PackageCategoryService {

    private final PackageCategoryRepository packageCategoryRepository;


    // 대분류 카테고리 리스트 반환
    public List<PackageCategory> getMainCategories() {
        return packageCategoryRepository.findByParentIsNull();
    }
    public List<PackageCategory> getSubCategories(int depth) {
        return packageCategoryRepository.findByDepth(depth);
    }

    public List<PackageCategory> getSubCategory(Long mainCategoryId) {
        return packageCategoryRepository.findByParentId(mainCategoryId);
    }

    public List<PackageCategory> getSmallCategoryId(Long subCategoryId) {
        return packageCategoryRepository.findByParentId(subCategoryId);
    }
}