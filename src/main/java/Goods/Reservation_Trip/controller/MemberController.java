package Goods.Reservation_Trip.controller;

import Goods.Reservation_Trip.dto.member.req.JoinDto;
import Goods.Reservation_Trip.service.member.MailService;
import Goods.Reservation_Trip.service.member.MemberService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.naming.AuthenticationException;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    private final MailService mailService;

    @GetMapping("/login")
    private String loginPage() {
        return "member/login";
    }

    @GetMapping("/terms/agree")
    private String agreePage() {
        return "member/agree";
    }

    @GetMapping("/join")
    private String joinPage() {
        return "member/join";
    }

    @PostMapping("/send/mail")
    @ResponseBody
    public String sendMail(JoinDto joinDto) throws MessagingException {

        //이메일 중복 확인 후 메일 발송
        //중복된경우
        if (!memberService.duplicatedEmail(joinDto.getEmail())) {
            return "600";
        }

        mailService.sendAuthCode(joinDto.getEmail());
        return "1000";


    }

    //인증번호 확인
    @PostMapping("/check/authCode")
    @ResponseBody
    public String checkCode(JoinDto joinDto) throws AuthenticationException {
        if (mailService.validationAuthCode(joinDto.getEmail(), joinDto.getAuthCode())) return "1000";
        return "500";
    }

    @PostMapping("/join")
    public String join(@Valid JoinDto joinDto, Model model, Errors errors) throws AuthenticationException {
        model.addAttribute("email", joinDto.getEmail());
        model.addAttribute("password", joinDto.getPassword());
        model.addAttribute("name", joinDto.getName());
        model.addAttribute("phoneNumber", joinDto.getPhoneNumber());
        model.addAttribute("gender", joinDto.isGender());
        model.addAttribute("birth", joinDto.getBirth());

        if (!mailService.validationAuthCode(joinDto.getEmail(), joinDto.getAuthCode())) {
            model.addAttribute("alert", "인증번호가 일치하지 않습니다.");
            return "member/join";
        }

        //이메일 중복된 경우
        if(!memberService.duplicatedEmail(joinDto.getEmail())){
            model.addAttribute("alert", "중복된 이메일입니다.");
            return "member/join";
        }

        memberService.join(joinDto);
        return "member/joinSuccess";
    }

}
