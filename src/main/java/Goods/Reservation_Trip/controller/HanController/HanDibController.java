package Goods.Reservation_Trip.controller.HanController;

import Goods.Reservation_Trip.dto.HanDto.DibPageDto;
import Goods.Reservation_Trip.dto.HanDto.HeaderDto;
import Goods.Reservation_Trip.service.HanService.HanDibService;
import Goods.Reservation_Trip.service.HanService.HanHeaderService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Collections;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HanDibController {

    private final HanDibService hanDibService;

    private final HanHeaderService hanHeaderService;

    //찜 목록 페이지로 이동
    @GetMapping("/member/dib")
    public String CartGo(HttpServletRequest request, Model model, RedirectAttributes rttr) {

        //헤더 카테고리랑 로그인 상태 및 유저이름 보내주는 서비스
        HeaderDto headerDto = hanHeaderService.HeaderCategoryAndMember(request);

        model.addAttribute("headerDto",headerDto);

        //서비스로 찜 리스트를 가져온다
        List<DibPageDto> DibPageDtoList = hanDibService.dibListGo(request);

        //null일 경우 서비스에서 문제 발생
        if(DibPageDtoList ==null){
            log.error("hanDibService.dibListGo 에서 문제가 발생했습니다");
            rttr.addFlashAttribute("data", "오류가 발생했습니다");
            return "redirect:/";
        }

        //하나도 없을 경우 찜목록이 비어있다는걸 보여줌
        if (DibPageDtoList.isEmpty()) {
            model.addAttribute("dibNo", 1);
        }

        //역순 정렬
        Collections.reverse(DibPageDtoList);

        model.addAttribute("DibPageDtoList", DibPageDtoList);

        return "dib";
    }


}
