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
     * 한국 공항 리스트
     */
    public PackageAirportResponseDto getKoreaAirportList() {
        //카테고리 아이디가 null이면 한국공항
        List<Airport> airportList = airportRepository.findByCategoryListIsEmpty();
        return PackageAirportResponseDto.builder()
                .sub("한국")
                .airport(airportList.stream().map(airport -> airport.getName() + "(" + airport.getCode() + ")").toList())
                .build();
    }

    /**
     * 카테고리 생성하기
     */
    public int registerCategory(PackageCategoryDto packageCategoryDto) {

        PackageCategory packageCategory = new PackageCategory();

        //이미 동명 카테고리가 존재한다면
        if (packageCategoryRepository.existsByName(packageCategoryDto.getCategoryName())) {
            return 100;
        }

        //등록하고싶은 카테고리가 대분류가 아니면
        if (packageCategoryDto.getDepth() != 1) {

            //부모 이름으로 카테고리검색
            Optional<PackageCategory> optionalPackageCategory = packageCategoryRepository.findByName(packageCategoryDto.getParentsName());

            if (optionalPackageCategory.isEmpty()) return 500;
            packageCategory = optionalPackageCategory.get();

            //바로 하위 카테고리로 선택했는지 확인
            if (packageCategoryDto.getDepth() - packageCategory.getDepth() != 1) {
                return 700;
            }
        } else {
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
    public int deleteCategory(String categoryName) {
        Optional<PackageCategory> optionalPackageCategory = packageCategoryRepository.findByName(categoryName);

        //일치하는 카테고리명이 없다면
        if (optionalPackageCategory.isEmpty()) return 100;

        PackageCategory packageCategory = optionalPackageCategory.get();

        //해당 카테고리에서 패키지가 존재한다면

        int packageSize = packageCategoryRepository.countByAnyCategory(packageCategory.getId());

        if (packageSize > 0) {
            return 500;
        }

        //하위 카테고리가 존재한다면
        if (!packageCategory.getChildren().isEmpty()) {
            return 700;
        }

        packageCategoryRepository.delete(packageCategory);

        return 1000;
    }

    /**
     * 공항 등록
     */
    public int registerAirport(PackageAirportDto airportDto) {

        //dto의 공항 코드를 대문자로 변경
        String code = airportDto.getAirportCode().toUpperCase();

        //일치하는 지역(카테고리)이 없는 경우
        //경우 1: 한국
        if (airportDto.getParentsName().equals("한국")) {

            //동일한 코드나 동일한 이름으로 등록 불가
            Optional<Airport> optionalAirport = airportRepository.findByNameOrCode(airportDto.getAirportName(), airportDto.getAirportCode());

            if (optionalAirport.isPresent()) return 100;

            Airport airport = Airport.builder()
                    .name(airportDto.getAirportName())
                    .code(code)
                    .build();

            airportRepository.save(airport);

            return 1000;
        }
        //경우 2 : 다른 나라
        //등록하고자 하는 지역이 소분류일때만 가능
        Optional<PackageCategory> optionalPackageCategory = packageCategoryRepository.findByNameAndDepth(airportDto.getParentsName(), 3);

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

            //한국이 아닌경우는 동일한 공항 추가 가능
            if (airport.getCategoryList() == null) {
                airport.setCategoryList(new ArrayList<>());
            }
            airport.getCategoryList().add(packageCategory);

            airportRepository.save(airport);

            return 1000;
        }

        Airport airport = Airport.builder()
                .name(airportDto.getAirportName())
                .code(code)
                .build();


        if (airport.getCategoryList() == null) {
            airport.setCategoryList(new ArrayList<>());
        }
        airport.getCategoryList().add(packageCategory);

        airportRepository.save(airport);

        return 1000;
    }

    /**
     * 공항 삭제
     */
    public int deleteAirport(PackageAirportDto airportDto) {

        Airport airport = new Airport();

        //공항명과 공항코드를 둘다 작성하였는데 일치하지 않을 때
        // 예)김해국제공항 코드가 PUS 인데 공항명은 김해국제공항으로 적어놓고 공항코드는 PSS로 했을 경우
        if (airportDto.getAirportName() != "" && airportDto.getAirportCode() != "") {
            Optional<Airport> optionalAirport = airportRepository.findByNameAndCode(airportDto.getAirportName(), airportDto.getAirportCode());

            if (optionalAirport.isEmpty()) return 100;

            airport = optionalAirport.get();

            //공항과 연결된 패키지 스케쥴이 있을 경우
            if (airportRepository.isAirportUsedRaw(airport.getId()) == 1) return 500;

        }

        //공항명으로 찾을경우 (공항코드가 null인경우)
        if (airportDto.getAirportCode() == "" && airportDto.getAirportName() != "") {
            Optional<Airport> optionalAirport = airportRepository.findByName(airportDto.getAirportName());
            if (optionalAirport.isEmpty()) return 300;

            airport = optionalAirport.get();

            //공항과 연결된 패키지 스케쥴이 있을 경우
            if (airportRepository.isAirportUsedRaw(airport.getId()) == 1) return 500;

        }

        //공항코드로 찾을경우 (공항명이 null인경우)
        if (airportDto.getAirportName() == "" && airportDto.getAirportCode() != "") {
            Optional<Airport> optionalAirport = airportRepository.findByCode(airportDto.getAirportCode());
            if (optionalAirport.isEmpty()) return 700;

            airport = optionalAirport.get();

            //공항과 연결된 패키지 스케쥴이 있을 경우
            if (airportRepository.isAirportUsedRaw(airport.getId()) == 1) return 500;

        }
        if (!airportDto.getParentsName().equals("한국")) {
        //해당지역이 없는 경우
        Optional<PackageCategory> optionalPackageCategory = packageCategoryRepository.findByName(airportDto.getParentsName());

        if (optionalPackageCategory.isEmpty()) return 900;

        PackageCategory packageCategory = optionalPackageCategory.get();

        //해당 지역에 연결되어있지 않은 경우

            if (!airportRepository.existsByCategoryIdAndNameOrCategoryIdAndCode
                    (packageCategory.getId(), airportDto.getAirportName(), packageCategory.getId(), airportDto.getAirportCode()))
                return 1100;

            airport.getCategoryList().remove(packageCategory);
            airportRepository.save(airport);

            return 1000;
        }

        //한국에 연결되어있지 않은 경우
        if (!airport.getCategoryList().isEmpty())
            return 1100;
        airportRepository.delete(airport);
        return 1000;

    }
}