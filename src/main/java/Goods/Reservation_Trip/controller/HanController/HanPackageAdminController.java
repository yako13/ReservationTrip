package Goods.Reservation_Trip.controller.HanController;

import Goods.Reservation_Trip.service.HanService.HanPackageService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HanPackageAdminController {

    private final HanPackageService hanPackageService;

    //패키지 삭제
    @GetMapping("/admin/package/delete/{id}")
    public String packDelete(@PathVariable("id") Long id, RedirectAttributes rttr) {

        if (id == null) {
            log.error("id =null 입니다");
            rttr.addFlashAttribute("data", "잘못된 접근입니다");
        }

        //패키지 삭제하는 서비스 (PackageStatus.CLOSED로 변경)
        int check = hanPackageService.packDeleteService(id);

        //check 가 0 ,1 실패 , 2 성공

        if (check == 0) {

            log.error("서비스 에서 문제가 발생했습니다");
            rttr.addFlashAttribute("data", "패키지를 찾을수 없습니다");
            return "redirect:/admin/package/list";
        }

        if (check == 1) {

            rttr.addFlashAttribute("data", "이미 삭제된 패키지입니다");
            return "redirect:/admin/package/list";
        }


        rttr.addFlashAttribute("data", "삭제가 완료되었습니다");

        return "redirect:/admin/package/list";
    }


    @GetMapping("/admin/package/delete12/{id}")
    public String packDelete2(@PathVariable("id") Long id,
                              @RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "20") int size,
                              @RequestParam(value = "keyword", required = false, defaultValue = "") String name,
                              @RequestParam(defaultValue = "default") String sort,
                              @RequestParam(required = false) Long mainCategoryId,
                              @RequestParam(required = false) Long subCategoryId,
                              @RequestParam(required = false) Long smallCategoryId,
                              RedirectAttributes rttr) {


        log.info("delete12 부분 호출");
        log.info("delete12 부분 호출 키워드 : " +  name);

        if (id == null) {
            log.error("id =null 입니다");
            rttr.addFlashAttribute("data", "잘못된 접근입니다");
        }

        // 쿼리 파라미터로 전달
        rttr.addAttribute("page", page);
        rttr.addAttribute("size", size);
        rttr.addAttribute("keyword", name);
        rttr.addAttribute("sort", sort);
        if (mainCategoryId != null) rttr.addAttribute("mainCategoryId", mainCategoryId);
        if (subCategoryId != null) rttr.addAttribute("subCategoryId", subCategoryId);
        if (smallCategoryId != null) rttr.addAttribute("smallCategoryId", smallCategoryId);


        //패키지 삭제하는 서비스 (PackageStatus.CLOSED 변경)
        int check = hanPackageService.packDeleteService(id);

        //check 가 0 ,1 실패 , 2 성공

        if (check == 0) {

            log.error("서비스 에서 문제가 발생했습니다");
            rttr.addFlashAttribute("data", "패키지를 찾을수 없습니다");
            return "redirect:/admin/package/search/index";
        }

        if (check == 1) {

            rttr.addFlashAttribute("data", "이미 삭제된 패키지입니다");
            return "redirect:/admin/package/search/index";
        }


        rttr.addFlashAttribute("data", "삭제가 완료되었습니다");

        return "redirect:/admin/package/search/index";
    }


}
