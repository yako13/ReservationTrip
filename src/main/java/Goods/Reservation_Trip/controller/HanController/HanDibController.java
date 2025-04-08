package Goods.Reservation_Trip.controller.HanController;

import Goods.Reservation_Trip.dto.HanDto.DibPageDto;
import Goods.Reservation_Trip.service.HanService.HanDibService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HanDibController {

    private final HanDibService hanDibService;



    //찜 목록 페이지로 이동
    @GetMapping("/dib")
    public String CartGo(HttpServletRequest request, Model model) {

        //서비스로 찜 리스트를 가져온다
        List<DibPageDto> DibPageDtoList = hanDibService.dibListGo(request);

        //null일 경우 서비스에서 문제 발생
        if(DibPageDtoList ==null){
            log.error("hanDibService.dibListGo 에서 문제가 발생했습니다");
        }

        //하나도 없을 경우 찜목록이 비어있다는걸 보여줌
        if (DibPageDtoList.isEmpty()) {
            model.addAttribute("dibNo", 1);
        }

        model.addAttribute("DibPageDtoList", DibPageDtoList);

        return "dib";
    }


}
