package Goods.Reservation_Trip.controller.HanController;

import Goods.Reservation_Trip.service.HanService.HanPackageService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

        //패키지 삭제하는 서비스 (PackageStatus.DELETE로 변경)
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


}
