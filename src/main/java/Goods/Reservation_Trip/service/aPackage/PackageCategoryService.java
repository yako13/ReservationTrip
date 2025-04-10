package Goods.Reservation_Trip.service.aPackage;

import Goods.Reservation_Trip.dto.aPackage.req.PackageCategoryDto;
import Goods.Reservation_Trip.dto.aPackage.res.PackageCategoryResponseDto;
import Goods.Reservation_Trip.entity.PackageCategory;
import Goods.Reservation_Trip.repository.aPackage.PackageCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PackageCategoryService {

    private final PackageCategoryRepository packageCategoryRepository;


    // 대분류 카테고리 리스트 반환
    public List<PackageCategory> getMainCategories() {
        return packageCategoryRepository.findByParentIsNull();
    }

    /**
     * 카테고리 목록 가져오기
     */
    public List<PackageCategoryResponseDto> getCategories() {

        List<PackageCategoryResponseDto> packageCategoryResponseDtoList = new ArrayList<>();

        //대분류 가져오기
        List<PackageCategory> majorList = packageCategoryRepository.findByParentIsNull();


        //중분류 가져오기
        for (PackageCategory packageCategory : majorList) {
            List<PackageCategory> mediumCategories = packageCategoryRepository.findByParentIdAndDepth(packageCategory.getId(), 2);
            List<String> mediumList = new ArrayList<>();
            List<List<String>> subList = new ArrayList<>();

            //소분류 가져오기
            for (PackageCategory packageCategory1 : mediumCategories) {
                List<String> subList2 = new ArrayList<>();
                String name = packageCategory1.getName();
                mediumList.add(name);

                List<PackageCategory> subCategories = packageCategoryRepository.findByParentIdAndDepth(packageCategory1.getId(), 3);

                for (PackageCategory packageCategory2 : subCategories) {
                    String subName = packageCategory2.getName();
                    subList2.add(subName);
                }
                subList.add(subList2);


            }
            PackageCategoryResponseDto packageCategoryResponseDto = PackageCategoryResponseDto.builder()
                    .major(packageCategory.getName())
                    .medium(mediumList)
                    .sub(subList)
                    .build();

            packageCategoryResponseDtoList.add(packageCategoryResponseDto);
        }

        return packageCategoryResponseDtoList;
    }

    /**
     * 카테고리 생성하기
     */
    public int registerCategory(PackageCategoryDto packageCategoryDto) {

        PackageCategory packageCategory = new PackageCategory();

        //이미 동명 카테고리가 존재한다면
        if(packageCategoryRepository.existsByName(packageCategoryDto.getCategoryName())){
            return 100;
        }

        //등록하고싶은 카테고리가 대분류가 아니면
        if (packageCategoryDto.getDepth() != 1) {

            //부모 이름으로 카테고리검색
            Optional<PackageCategory> optionalPackageCategory = packageCategoryRepository.findByName(packageCategoryDto.getParentsName());

            if (optionalPackageCategory.isEmpty()) return 500;
            packageCategory = optionalPackageCategory.get();

            //바로 하위 카테고리로 선택했는지 확인
            if(packageCategoryDto.getDepth() - packageCategory.getDepth() !=1 ){
                return 700;
            }
        }
        else {
            packageCategory = null;
        }

        PackageCategory packageCategory2 = PackageCategory.builder()
                .depth(packageCategoryDto.getDepth())
                .parent(packageCategory)
                .name(packageCategoryDto.getCategoryName())
                .build();

        packageCategoryRepository.save(packageCategory2);

        return 1000;
    }

    /**
     * 카테고리 삭제
     */
    public int deleteCategory(String categoryName){
        Optional<PackageCategory> optionalPackageCategory = packageCategoryRepository.findByName(categoryName);

        //일치하는 카테고리명이 없다면
        if(optionalPackageCategory.isEmpty()) return 100;

        PackageCategory packageCategory = optionalPackageCategory.get();

        //해당 카테고리에서 패키지가 존재한다면

        int packageSize = packageCategoryRepository.countByAnyCategory(packageCategory.getId());

        if(packageSize>0){
            return 500;
        }
        
        //하위 카테고리가 존재한다면
        if(!packageCategory.getChildren().isEmpty()){
            return 700;
        }

        packageCategoryRepository.delete(packageCategory);

        return 1000;
    }
}