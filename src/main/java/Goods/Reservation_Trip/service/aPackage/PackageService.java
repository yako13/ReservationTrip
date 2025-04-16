package Goods.Reservation_Trip.service.aPackage;

import Goods.Reservation_Trip.dto.aPackage.req.CategoryDto;
import Goods.Reservation_Trip.dto.aPackage.req.PackageOptionRequestDto;
import Goods.Reservation_Trip.dto.aPackage.req.PackageRequestDto;
import Goods.Reservation_Trip.dto.aPackage.req.PackageScheduleRequestDto;
import Goods.Reservation_Trip.dto.aPackage.res.PackageEditResponseDto;
import Goods.Reservation_Trip.dto.aPackage.res.PackageOptionEditResponseDto;
import Goods.Reservation_Trip.dto.aPackage.res.PackageScheduleEditResponseDto;
import Goods.Reservation_Trip.entity.Package;
import Goods.Reservation_Trip.entity.PackageCategory;
import Goods.Reservation_Trip.entity.PackageImage;
import Goods.Reservation_Trip.entity.PackageSchedule;
import Goods.Reservation_Trip.enums.PackageImageType;
import Goods.Reservation_Trip.enums.PackageStatus;
import Goods.Reservation_Trip.repository.aPackage.PackageCategoryRepository;
import Goods.Reservation_Trip.repository.aPackage.PackageImageRepository;
import Goods.Reservation_Trip.repository.aPackage.PackageRepository;
import Goods.Reservation_Trip.util.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PackageService {

    private final PackageRepository packageRepository;

    private final PackageImageService packageImageService;

    private final PackageScheduleService packageScheduleService;

    private final PackageOptionService packageOptionService;

    private final PackageCategoryRepository packageCategoryRepository;

    private final FileStorageService fileStorageService;

    private final PackageImageRepository packageImageRepository;

    // 패키지 저장
    @Transactional
    public void save(PackageRequestDto requestDto,
                     PackageOptionRequestDto optionRequestDto,
                     List<PackageScheduleRequestDto> scheduleRequestDto) {

        PackageCategory mainCategory = packageCategoryRepository.findById(requestDto.getMainCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("대분류 카테고리를 찾을 수 없습니다."));
        PackageCategory subCategory = packageCategoryRepository.findById(requestDto.getSubCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("중분류 카테고리를 찾을 수 없습니다."));
        PackageCategory smallCategory = packageCategoryRepository.findById(requestDto.getSmallCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("소분류 카테고리를 찾을 수 없습니다."));

        Package aPackage = Package.builder()
                .packageName(requestDto.getPackageName())
                .adultPrice(requestDto.getAdultPrice())
                .childPrice(requestDto.getChildPrice())
                .babyPrice(requestDto.getBabyPrice())
                .fuelSurcharge(requestDto.getFuelSurcharge())
                .fuelSurchargeIncluded(requestDto.getFuelSurchargeIncluded())
                .description(requestDto.getDescription())
                .period(requestDto.getPeriod())
                .hotelName(requestDto.getHotelName())
                .mainCategory(mainCategory)
                .subCategory(subCategory)
                .smallCategory(smallCategory)
                .packageStatus(PackageStatus.AVAILABLE)
                .packageScheduleList(new ArrayList<>())
                .build();

        // 패키지 기본 정보 저장
        packageRepository.save(aPackage);

        if (requestDto.getMainImage() != null && !requestDto.getMainImage().isEmpty()) {
            PackageImage mainPackageImage = packageImageService.create(requestDto, aPackage);
            aPackage.setMainImage(mainPackageImage);
        }
        // 선택 옵션 저장
        packageOptionService.save(aPackage, optionRequestDto);
        // 항공편 일정 정보 리스트 저장
        List<PackageSchedule> scheduleList = packageScheduleService.saveAll(aPackage, scheduleRequestDto);
        aPackage.addPackageSchedules(scheduleList);
    }

    // 패키지 id
    public Package getPackage(Long id) {
        Optional<Package> optPackage = packageRepository.findById(id);

        if (optPackage.isEmpty()) throw new RuntimeException("해당 상품 없음");

        return optPackage.get();
    }

    public PackageEditResponseDto getPackageEdit(Long id) {
        Package aPackage = getPackage(id);

        List<PackageScheduleEditResponseDto> schedules = packageScheduleService.getPackageScheduleEdit(id);

        List<PackageOptionEditResponseDto> options = packageOptionService.getPackageOption(id);

        return PackageEditResponseDto.builder()
                .id(aPackage.getId())
                .packageName(aPackage.getPackageName())
                .adultPrice(aPackage.getAdultPrice())
                .childPrice(aPackage.getChildPrice())
                .babyPrice(aPackage.getBabyPrice())
                .fuelSurcharge(aPackage.getFuelSurcharge())
                .fuelSurchargeIncluded(aPackage.getFuelSurchargeIncluded())
                .description(aPackage.getDescription())
                .period(aPackage.getPeriod())
                .packageStatus(aPackage.getPackageStatus())
                .mainImage(aPackage.getMainImage())
                .subImage(aPackage.getPackageImageList().stream()
                        .filter(packageImage -> packageImage.getPackageImageType() == PackageImageType.SUB).toList())
                .descImage(aPackage.getPackageImageList().stream()
                        .filter(packageImage -> packageImage.getPackageImageType() == PackageImageType.DESC).toList())
                .hotelName(aPackage.getHotelName())
                .mainCategoryId(CategoryDto.builder()
                        .id(aPackage.getMainCategory().getId())
                        .name(aPackage.getMainCategory().getName())
                        .build())
                .subCategoryId(CategoryDto.builder()
                        .id(aPackage.getSubCategory().getId())
                        .name(aPackage.getSubCategory().getName())
                        .build())
                .smallCategoryId(CategoryDto.builder()
                        .id(aPackage.getSmallCategory().getId())
                        .name(aPackage.getSmallCategory().getName())
                        .build())
                .schedules(schedules)
                .options(options)
                .build();
    }

    @Transactional
    public Package update(PackageRequestDto requestDto,
                          PackageOptionRequestDto optionRequestDto,
                          List<PackageScheduleRequestDto> scheduleRequestDto,
                          String deletedImages,
                          Long id) {
        Package aPackage = getPackage(id);

        PackageCategory mainCategory = packageCategoryRepository.findById(requestDto.getMainCategoryId())
                .orElseThrow(()-> new IllegalArgumentException("대분류 카테고리를 찾을 수 없습니다."));
        PackageCategory subCategory = packageCategoryRepository.findById(requestDto.getSubCategoryId())
                .orElseThrow(()-> new IllegalArgumentException("중분류 카테고리를 찾을 수 없습니다."));
        PackageCategory smallCategory = packageCategoryRepository.findById(requestDto.getSmallCategoryId())
                .orElseThrow(()-> new IllegalArgumentException("소분류 카테고리를 찾을 수 없습니다."));

        aPackage.setPackageName(requestDto.getPackageName());
        aPackage.setAdultPrice(requestDto.getAdultPrice());
        aPackage.setChildPrice(requestDto.getChildPrice());
        aPackage.setBabyPrice(requestDto.getBabyPrice());
        aPackage.setFuelSurcharge(requestDto.getFuelSurcharge());
        aPackage.setFuelSurchargeIncluded(requestDto.getFuelSurchargeIncluded());
        aPackage.setDescription(requestDto.getDescription());
        aPackage.setPeriod(requestDto.getPeriod());
        aPackage.setHotelName(requestDto.getHotelName());
        aPackage.setMainCategory(mainCategory);
        aPackage.setSubCategory(subCategory);
        aPackage.setSmallCategory(smallCategory);
        aPackage.setPackageStatus(requestDto.getPackageStatus());

        List<PackageImage> subImageList = aPackage.getPackageImageList().stream()
                .filter(image -> image.getPackageImageType() == PackageImageType.SUB)
                .collect(Collectors.toList());
        List<PackageImage> descImageList = aPackage.getPackageImageList().stream()
                .filter(image -> image.getPackageImageType() == PackageImageType.DESC)
                .collect(Collectors.toList());

        if (deletedImages != null && !deletedImages.trim().isEmpty()) {
            String[] deletedImageList = deletedImages.split(",");

            for (String deletedImage : deletedImageList) {
                if (deletedImage.isEmpty()) continue;

                if (deletedImage.startsWith("subImage")) {
                    try {
                        int index = Integer.parseInt(deletedImage.replace("subImage", ""));
                        if (index < subImageList.size()) {
                            PackageImage imageToDelete = subImageList.get(index);
                            fileStorageService.deleteFile(imageToDelete.getImageFullName());

                            packageImageRepository.delete(imageToDelete);
                            aPackage.getPackageImageList().remove(imageToDelete);
                        }
                    } catch (NumberFormatException e) {
                        System.err.println("Invalid subImage index:" + deletedImage);
                    }
                }
                if (deletedImage.startsWith("descImage")) {
                    try {
                        int index = Integer.parseInt(deletedImage.replace("descImage", ""));
                        if (index < descImageList.size()) {
                            PackageImage imageToDelete = descImageList.get(index);
                            fileStorageService.deleteFile(imageToDelete.getImageFullName());

                            packageImageRepository.delete(imageToDelete);
                            aPackage.getPackageImageList().remove(imageToDelete);
                        }
                    } catch (NumberFormatException e) {
                        System.err.println("Invalid descImage index:" + deletedImage);
                    }
                }
            }
        }
        PackageImage updatedImage = packageImageService.create(requestDto, aPackage);

        if (updatedImage != null) {
            updatedImage = packageImageRepository.save(updatedImage);
            aPackage.setMainImage(updatedImage);
        }

        packageRepository.save(aPackage);

        packageOptionService.save(aPackage, optionRequestDto);

        List<PackageSchedule> scheduleList = packageScheduleService.saveAll(aPackage, scheduleRequestDto);
        aPackage.addPackageSchedules(scheduleList);

        return aPackage;
    }
}
