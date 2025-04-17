package Goods.Reservation_Trip.service.aPackage;

import Goods.Reservation_Trip.dto.aPackage.req.AirportDto;
import Goods.Reservation_Trip.dto.aPackage.req.PackageAirportDto;
import Goods.Reservation_Trip.dto.aPackage.req.PackageCategoryDto;
import Goods.Reservation_Trip.dto.aPackage.res.PackageAirportResponseDto;
import Goods.Reservation_Trip.dto.aPackage.res.PackageCategoryResponseDto;
import Goods.Reservation_Trip.entity.Airport;
import Goods.Reservation_Trip.entity.PackageCategory;
import Goods.Reservation_Trip.repository.aPackage.AirportRepository;
import Goods.Reservation_Trip.repository.aPackage.PackageCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PackageCategoryAndAirportService {

    private final PackageCategoryRepository packageCategoryRepository;

    private final AirportRepository airportRepository;


    // 대분류 카테고리 리스트 반환
    public List<PackageCategory> getMainCategories() {
        return packageCategoryRepository.findByParentIsNull();
    }

    /**
     * 카테고리 목록 가져오기
     */
    public List<PackageCategoryResponseDto> getPackageCategoryResponseDtos() {
        List<PackageCategoryResponseDto> responseList = new ArrayList<>();

        //대분류 리스트 가져오기
        List<PackageCategory> majorCategories = packageCategoryRepository.findByParentIsNull();

        //그 대분류 리스트에서 중분류 리스트 가져오기
        for (PackageCategory major : majorCategories) {
            List<PackageCategory> mediumCategories = packageCategoryRepository.findByParentIdAndDepth(major.getId(), 2);

            List<String> mediumNames = new ArrayList<>();
            List<List<PackageAirportResponseDto>> allSubList = new ArrayList<>();

            //그 중분류 리스트에서 소분류 가져오기
            for (PackageCategory medium : mediumCategories) {
                mediumNames.add(medium.getName());
                //소분류 리스트 추가
                List<PackageAirportResponseDto> subList = buildSubList(medium.getId());
                allSubList.add(subList);
            }

            PackageCategoryResponseDto dto = PackageCategoryResponseDto.builder()
                    .major(major.getName())
                    .medium(mediumNames)
                    .sub(allSubList)
                    .build();

            responseList.add(dto);
        }

        return responseList;
    }

    /**
     * 소분류 리스트(+공항)
     */
    public List<PackageAirportResponseDto> buildSubList(Long parentId) {
        List<PackageAirportResponseDto> subList = new ArrayList<>();

        List<PackageCategory> subCategories = packageCategoryRepository.findByParentIdAndDepth(parentId, 3);

        for (PackageCategory subCategory : subCategories) {
            String subName = subCategory.getName();
            List<String> airportNames = getAirportNames(subCategory);

            PackageAirportResponseDto dto = PackageAirportResponseDto.builder()
                    .sub(subName)
                    .airport(airportNames)
                    .build();

            subList.add(dto);
        }

        return subList;
    }

    /**
     * 공항 리스트 가져오기
     */
    public List<String> getAirportNames(PackageCategory category) {
        return category.getAirportList()
                .stream()
                .map(airport -> airport.getName() + "(" + airport.getCode() + ")") //공항이름(공항코드) 형식으로 변환
                .collect(Collectors.toList());
    }


    /**
     * 카테고리 생성하기
     */
    public int registerCategory(PackageCategoryDto packageCategoryDto) {

        //동일한 이름의 카테고리 추가 불가
        if (packageCategoryRepository.existsByName(packageCategoryDto.getCategoryName())) return 100;


        if (packageCategoryDto.getMain().equals("대분류")) {
            PackageCategory packageCategory = PackageCategory.builder()
                    .depth(1)
                    .name(packageCategoryDto.getCategoryName())
                    .build();

            packageCategoryRepository.save(packageCategory);
            return 1000;
        }

        if (packageCategoryDto.getSub().equals("중분류")) {

            Optional<PackageCategory> mainPackageCategory = packageCategoryRepository.findByNameAndDepth(packageCategoryDto.getMain(), 1);

            //존재하지않는 상위카테고리
            if (mainPackageCategory.isEmpty()) return 300;

            PackageCategory packageCategory = PackageCategory.builder()
                    .depth(2)
                    .name(packageCategoryDto.getCategoryName())
                    .parent(mainPackageCategory.get())
                    .build();

            packageCategoryRepository.save(packageCategory);
            return 1000;
        }

        if (packageCategoryDto.getSmall().equals("소분류")) {
            Optional<PackageCategory> mainPackageCategory = packageCategoryRepository.findByNameAndDepth(packageCategoryDto.getMain(), 1);
            Optional<PackageCategory> subPackageCategory = packageCategoryRepository.findByNameAndDepth(packageCategoryDto.getSub(), 2);

            //존재하지않는 상위카테고리
            if (mainPackageCategory.isEmpty()) return 300;
            if (subPackageCategory.isEmpty()) return 300;

            PackageCategory packageCategory = PackageCategory.builder()
                    .depth(3)
                    .name(packageCategoryDto.getCategoryName())
                    .parent(subPackageCategory.get())
                    .build();

            packageCategoryRepository.save(packageCategory);
            return 1000;
        }


        return 1000;
    }

    /**
     * 카테고리 삭제
     */
    public int deleteCategory(PackageCategoryDto packageCategoryDto) {

        //옵션이 '대분류'가 선택되었다면
        if (packageCategoryDto.getMain().equals("대분류")) return 200;

        if (packageCategoryDto.getSub().equals("중분류")) {

            Optional<PackageCategory> mainCategory = packageCategoryRepository.findByName(packageCategoryDto.getMain());

            if (mainCategory.isEmpty()) return 300;

            PackageCategory main = mainCategory.get();

            //우선 패키지랑 연결되어있는지 확인
            if (packageCategoryRepository.countByAnyCategory(main.getId()) > 0) return 900;

            //자식 카테고리가 있는지 확인
            if (!main.getChildren().isEmpty()) return 700;

            packageCategoryRepository.delete(main);
            return 1000;
        }

        if (packageCategoryDto.getSmall().equals("소분류")) {

            Optional<PackageCategory> subCategory = packageCategoryRepository.findByName(packageCategoryDto.getSub());

            if (subCategory.isEmpty()) return 300;

            PackageCategory sub = subCategory.get();

            //우선 패키지랑 연결되어있는지 확인
            if (packageCategoryRepository.countByAnyCategory(sub.getId()) > 0) return 900;

            //자식 카테고리가 있는지 확인
            if (!sub.getChildren().isEmpty()) return 700;

            packageCategoryRepository.delete(sub);
            return 1000;
        } else {
            Optional<PackageCategory> smallCategory = packageCategoryRepository.findByName(packageCategoryDto.getSmall());

            if (smallCategory.isEmpty()) return 300;

            PackageCategory small = smallCategory.get();

            //우선 패키지랑 연결되어있는지 확인
            if (packageCategoryRepository.countByAnyCategory(small.getId()) > 0) return 900;

            packageCategoryRepository.delete(small);
            return 1000;
        }


    }

    /**
     * 카테고리 이름 수정
     */
    public int editCategory(PackageCategoryDto packageCategoryDto) {

        if (packageCategoryDto.getSub().equals("중분류")) {
            Optional<PackageCategory> optionalPackageCategory = packageCategoryRepository.findByNameAndDepth(packageCategoryDto.getMain(), 1);

            //수정하고자 하는 카테고리가 없는 경우
            if (optionalPackageCategory.isEmpty()) return 100;

            PackageCategory originalCategory = optionalPackageCategory.get();

            //수정하고자 하는 카테고리 이름이 이미 등록 된 경우
            Optional<PackageCategory> editOptionalPackageCategory = packageCategoryRepository.findByName(packageCategoryDto.getCategoryName());

            if (editOptionalPackageCategory.isPresent()) return 300;

            originalCategory.setName(packageCategoryDto.getCategoryName());

            packageCategoryRepository.save(originalCategory);

            return 1000;

        }

        if (packageCategoryDto.getSmall().equals("소분류")) {
            Optional<PackageCategory> optionalPackageCategory = packageCategoryRepository.findByNameAndDepth(packageCategoryDto.getSub(), 2);

            //수정하고자 하는 카테고리가 없는 경우
            if (optionalPackageCategory.isEmpty()) return 100;

            PackageCategory originalCategory = optionalPackageCategory.get();

            //수정하고자 하는 카테고리 이름이 이미 등록 된 경우
            Optional<PackageCategory> editOptionalPackageCategory = packageCategoryRepository.findByName(packageCategoryDto.getCategoryName());

            if (editOptionalPackageCategory.isPresent()) return 300;

            originalCategory.setName(packageCategoryDto.getCategoryName());

            packageCategoryRepository.save(originalCategory);

            return 1000;
        } else {
            Optional<PackageCategory> optionalPackageCategory = packageCategoryRepository.findByNameAndDepth(packageCategoryDto.getSmall(), 3);

            //수정하고자 하는 카테고리가 없는 경우
            if (optionalPackageCategory.isEmpty()) return 100;

            PackageCategory originalCategory = optionalPackageCategory.get();

            //수정하고자 하는 카테고리 이름이 이미 등록 된 경우
            Optional<PackageCategory> editOptionalPackageCategory = packageCategoryRepository.findByName(packageCategoryDto.getCategoryName());

            if (editOptionalPackageCategory.isPresent()) return 300;

            originalCategory.setName(packageCategoryDto.getCategoryName());

            packageCategoryRepository.save(originalCategory);

            return 1000;
        }

    }

    /**
     * 카테고리 위치 수정
     */
    public int editCategoryLocation(PackageCategoryDto packageCategoryDto) {

        PackageCategory category = null;

        if (packageCategoryDto.getCategoryName() != null && !packageCategoryDto.getCategoryName().trim().isEmpty()) {
            // 이름이 입력된 경우 → 수정된 이름 기준으로 대상 찾기
            Optional<PackageCategory> optCategory = packageCategoryRepository.findByName(packageCategoryDto.getCategoryName());
            if (optCategory.isEmpty()) return 100;
            category = optCategory.get();
        } else {
            // 이름 입력이 없는 경우 → 기존 값(main, sub, small)로 대상 찾기
            if (!packageCategoryDto.getSmall().equals("소분류")) {
                Optional<PackageCategory> opt = packageCategoryRepository.findByNameAndDepth(packageCategoryDto.getSmall(), 3);
                if (opt.isEmpty()) return 100;
                category = opt.get();
            } else if (!packageCategoryDto.getSub().equals("중분류")) {
                Optional<PackageCategory> opt = packageCategoryRepository.findByNameAndDepth(packageCategoryDto.getSub(), 2);
                if (opt.isEmpty()) return 100;
                category = opt.get();
            } else {
                Optional<PackageCategory> opt = packageCategoryRepository.findByNameAndDepth(packageCategoryDto.getMain(), 1);
                if (opt.isEmpty()) return 100;
                category = opt.get();
            }
        }

        int depth = category.getDepth();


        if (depth == 1) { // 대분류
            PackageCategory main = category;
            //자식이 존재할 경우
            if (!main.getChildren().isEmpty()) return 400;
            //패키지가 존재 할 경우
            if (!main.getMainCategoryPackages().isEmpty()) return 500;

            if (packageCategoryDto.getMainEdit().equals("대분류")) return 600;

            if (packageCategoryDto.getSubEdit().equals("중분류")) {
                Optional<PackageCategory> moveTo = packageCategoryRepository.findByNameAndDepth(packageCategoryDto.getMainEdit(), 1);
                if (moveTo.isEmpty()) return 700;

                PackageCategory parent = moveTo.get();
                main.setDepth(2);
                main.setParent(parent);
                packageCategoryRepository.save(main);
                return 1000;
            }

            Optional<PackageCategory> moveTo = packageCategoryRepository.findByNameAndDepth(packageCategoryDto.getSubEdit(), 2);
            if (moveTo.isEmpty()) return 700;

            PackageCategory parent = moveTo.get();
            main.setDepth(3);
            main.setParent(parent);
            packageCategoryRepository.save(main);
            return 1000;
        }

        if (depth == 2) { // 중분류
            PackageCategory sub = category;

            if (!sub.getChildren().isEmpty()) return 400;
            if (!sub.getSubCategoryPackages().isEmpty() ) return 500;

            if (packageCategoryDto.getMainEdit().equals("대분류")) {
                sub.setDepth(1);
                sub.setParent(null);
                packageCategoryRepository.save(sub);
                return 1000;
            }

            if (packageCategoryDto.getSubEdit().equals("중분류")) {
                Optional<PackageCategory> mainOpt = packageCategoryRepository.findByNameAndDepth(packageCategoryDto.getMainEdit(), 1);
                if (mainOpt.isEmpty()) return 700;

                PackageCategory main = mainOpt.get();
                sub.setParent(main);
                sub.setDepth(2);
                packageCategoryRepository.save(sub);
                return 1000;
            }

            Optional<PackageCategory> moveTo = packageCategoryRepository.findByNameAndDepth(packageCategoryDto.getSubEdit(), 2);
            if (moveTo.isEmpty()) return 700;

            PackageCategory parent = moveTo.get();
            sub.setDepth(3);
            sub.setParent(parent);
            packageCategoryRepository.save(sub);
            return 1000;
        }

        if (depth == 3) { // 소분류
            PackageCategory small = category;

            if (!small.getSmallCategoryPackages().isEmpty()) return 500;

            if (packageCategoryDto.getMainEdit().equals("대분류")) {
                small.setDepth(1);
                small.setParent(null);
                packageCategoryRepository.save(small);
                return 1000;
            }

            if (packageCategoryDto.getSubEdit().equals("중분류")) {
                Optional<PackageCategory> moveTo = packageCategoryRepository.findByNameAndDepth(packageCategoryDto.getMainEdit(), 1);
                if (moveTo.isEmpty()) return 700;

                PackageCategory parent = moveTo.get();
                small.setDepth(2);
                small.setParent(parent);
                packageCategoryRepository.save(small);
                return 1000;
            }

            Optional<PackageCategory> moveTo = packageCategoryRepository.findByNameAndDepth(packageCategoryDto.getSubEdit(), 2);
            if (moveTo.isEmpty()) return 700;

            PackageCategory parent = moveTo.get();
            small.setDepth(3);
            small.setParent(parent);
            packageCategoryRepository.save(small);
            return 1000;
        }
        else return 1500;

    }


    /**
     * 공항 등록
     */
    public int registerAirport(PackageAirportDto airportDto) {

        //dto의 공항 코드를 대문자로 변경
        String code = airportDto.getAirportCode().toUpperCase();

        //등록하고자 하는 지역이 소분류일때만 가능
        Optional<PackageCategory> optionalPackageCategory = packageCategoryRepository.findByNameAndDepth(airportDto.getSmall(), 3);

        if (optionalPackageCategory.isEmpty()) return 500;

        PackageCategory packageCategory = optionalPackageCategory.get();

        //공항명 또는 공항코드가 존재하지만 일치하지 않을 때
        // 예)김해국제공항 코드가 PUS 인데 공항명은 김해국제공항으로 적어놓고 공항코드는 PSS로 했을 경우
        Optional<Airport> optionalAirport2 = airportRepository.findByNameOrCode(airportDto.getAirportName(), airportDto.getAirportCode());
        if (optionalAirport2.isPresent()) {
            Optional<Airport> optionalAirport = airportRepository.findByNameAndCode(airportDto.getAirportName(), airportDto.getAirportCode());

            if (optionalAirport.isEmpty()) return 300;

            Airport airport = optionalAirport.get();

            //같은 카테고리에는 등록 불가
            if (airportRepository.existsByCategoryIdAndNameOrCategoryIdAndCode
                    (packageCategory.getId(), airportDto.getAirportName(), packageCategory.getId(), airportDto.getAirportCode()))
                return 400;

            //동일한 공항 다른지역에 추가 가능
            if (airport.getCategoryList() == null) {
                airport.setCategoryList(new ArrayList<>());
            }
            airport.getCategoryList().add(packageCategory);

            airportRepository.save(airport);

            return 1000;
        }

        Airport airport = new Airport();

        airport.setCategoryList(List.of(packageCategory));
        airport.setName(airportDto.getAirportName());
        airport.setCode(airportDto.getAirportCode());

        airportRepository.save(airport);

        return 1000;
    }

    /**
     * 공항 삭제
     */
    public int deleteAirport(PackageAirportDto airportDto) {

        Optional<PackageCategory> optionalPackageCategory = packageCategoryRepository.findByNameAndDepth(airportDto.getSmall(), 3);

        if (optionalPackageCategory.isEmpty()) return 900;

        PackageCategory packageCategory = optionalPackageCategory.get();

        //해당 지역에 연결되어있는지 확인
        Optional<Airport> optionalAirport = airportRepository.findByCategoryIdAndName(packageCategory.getId(), airportDto.getAirportName());
        if (optionalAirport.isEmpty()) return 1100;

        Airport airport = optionalAirport.get();

        //패키지랑 연결되어있는지 확인
        if (airportRepository.isAirportUsedRaw(airport.getId()) > 0) return 500;

        airportRepository.delete(airport);
        return 1000;

    }

    public List<String> collectCategoryNames(PackageCategory category) {
        List<String> names = new ArrayList<>();
        collectNamesRecursive(category, names);
        return names;
    }

    private void collectNamesRecursive(PackageCategory category, List<String> names) {
        names.add(category.getName());
        for (PackageCategory child : category.getChildren()) {
            collectNamesRecursive(child, names);
        }
    }
}