package Goods.Reservation_Trip.config;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());
            model.addAttribute("status", statusCode); // 상태 코드 추가

            switch (statusCode) {
                case 400 -> {
                    model.addAttribute("message", "잘못된 요청입니다.");
                    break;
                }
                case 401 -> {
                    model.addAttribute("message", "인증이 필요합니다. 로그인 해주세요.");
                    break;
                }
                case 403 -> {
                    model.addAttribute("message", "접근 권한이 없습니다.");
                    break;
                }
                case 404 -> {
                    model.addAttribute("message", "페이지를 찾을 수 없습니다");
                    break;
                }
                case 500 -> {
                    model.addAttribute("message", "서버 오류가 발생했습니다. 잠시 후 다시 시도해주세요.");
                    break;
                }
                default -> {
                    model.addAttribute("message", "예기치 못한 오류가 발생했습니다. (오류 코드 : " + statusCode + ")");
                    break;
                }
            }
        }
        return "error/custom-error";
    }
}
