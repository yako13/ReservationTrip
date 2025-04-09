package Goods.Reservation_Trip.service.aPackage;

import Goods.Reservation_Trip.config.ImageManager;
import Goods.Reservation_Trip.dto.aPackage.res.PackageImageUrlDto;
import Goods.Reservation_Trip.dto.aPackage.req.PackageRequestDto;
import Goods.Reservation_Trip.entity.Package;
import Goods.Reservation_Trip.entity.PackageImage;
import Goods.Reservation_Trip.enums.PackageImageType;
import Goods.Reservation_Trip.repository.aPackage.PackageImageRepository;
import Goods.Reservation_Trip.util.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
@RequiredArgsConstructor
public class PackageImageService {

    private final PackageImageRepository packageImageRepository;

    private final ImageManager imageManager;

    private final FileStorageService fileStorageService;

    @Transactional
    public PackageImage save(MultipartFile imageFile, Package aPackage, PackageImageType imageType){
        if (imageFile.isEmpty()) return null;

        UUID imageUuid = imageManager.save(imageFile);

        PackageImage packageImage = PackageImage.builder()
                .uuid(imageUuid)
                .fileExtension(imageManager.getExtensionOf(imageFile))
                .packageImageType(imageType)
                .originalName(imageFile.getOriginalFilename())
                .aPackage(aPackage)
                .build();

        return packageImageRepository.save(packageImage);
    }

    public void multiSave(List<MultipartFile> imageFiles, Package aPackage, PackageImageType imageType) {
        if (imageFiles == null || imageFiles.isEmpty()) return;

        imageFiles.forEach(imageFile -> save(imageFile, aPackage, imageType));
    }

    @Transactional
    public void updateSubAndDescImages(PackageRequestDto requestDto, Package aPackage) {
        List<PackageImage> existingImages = new ArrayList<>(Optional.ofNullable(aPackage.getPackageImageList())
                .orElse(Collections.emptyList())
                .stream()
                .filter(img -> img.getPackageImageType() == PackageImageType.SUB || img.getPackageImageType() == PackageImageType.DESC)
                .toList());

        if (requestDto.getSubImage() != null && requestDto.getSubImage().isEmpty()) {
            PackageImage oldImage = aPackage.getMainImage();
            if (oldImage != null) {
                fileStorageService.deleteFile(oldImage.getImageFullName());
                existingImages.forEach(image -> fileStorageService.deleteFile(image.getImageFullName()));

                packageImageRepository.deleteAll(existingImages);
                packageImageRepository.flush();
            }
        }

        if (requestDto.getSubImage() != null && !requestDto.getSubImage().isEmpty()) {
            multiSave(requestDto.getSubImage(), aPackage, PackageImageType.SUB);
        }
        if (requestDto.getDescImage() != null && !requestDto.getDescImage().isEmpty()) {
            multiSave(requestDto.getDescImage(), aPackage, PackageImageType.DESC);
        }
    }

    @Transactional
    public PackageImage create(PackageRequestDto requestDto, Package aPackage) {
        updateSubAndDescImages(requestDto, aPackage);

        if (requestDto.getMainImage() != null && !requestDto.getMainImage().isEmpty()) {
            PackageImage oldImage = aPackage.getMainImage();
            if (oldImage != null) {
                fileStorageService.deleteFile(oldImage.getImageFullName());

                packageImageRepository.delete(oldImage);
                packageImageRepository.flush();
            }
            return save(requestDto.getMainImage(), aPackage, PackageImageType.MAIN);
        }
        return aPackage.getMainImage();
    }

    @Transactional
    public PackageImageUrlDto getPackageImageDto(Long aPackageId) {
        List<PackageImage> packageImageList = packageImageRepository.findByaPackageId(aPackageId);

        String packageMainImageUrl = null;
        List<String> packageSubImageUrl = new ArrayList<>();
        List<String> packageDescImageUrl = new ArrayList<>();

        for (PackageImage packageImage : packageImageList) {
            String imageUrl = imageManager.createImageUrl(packageImage.getImageFullName());
            switch (packageImage.getPackageImageType()) {
                case MAIN -> packageMainImageUrl = imageUrl;
                case SUB -> packageSubImageUrl.add(imageUrl);
                case DESC -> packageDescImageUrl.add(imageUrl);
            }
        }
        return PackageImageUrlDto.builder()
                .mainImageUrl(packageMainImageUrl)
                .subImageUrl(packageSubImageUrl)
                .descImageUrl(packageDescImageUrl)
                .build();
    }
}
