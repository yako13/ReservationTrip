package Goods.Reservation_Trip.controller;

import Goods.Reservation_Trip.enums.PackageCategory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class PackageApiController {

    @GetMapping("api/categories")
    public List<PackageCategory> getCategories(){
        return Arrays.stream(PackageCategory.values())
                .filter(category -> category.getParent() == null)
                .collect(Collectors.toList());
    }

    @GetMapping("api/categories/{parentCategory}")
    public List<PackageCategory> getSubCategories(@PathVariable String parentCategory) {
        PackageCategory parent = PackageCategory.valueOf(parentCategory);
        return PackageCategory.getSubCategories(parent);
    }

    @GetMapping("/api/categories/{parentCategory}/{subCategory}")
    public List<PackageCategory> getSmallCategories(@PathVariable String parentCategory, @PathVariable String subCategory){
        PackageCategory parent = PackageCategory.valueOf(parentCategory);
        PackageCategory sub = PackageCategory.valueOf(subCategory);
        return PackageCategory.getSmallCategories(sub);
    }
}
