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

    //아이디 찾기 페이지
    @GetMapping("/find/id")
    public String findIdPage(){
        return "member/findId";
    }

    //아이디찾기
    @PostMapping("/find/id")
    @ResponseBody
    public String findId(JoinDto joinDto){
        //아이디를 찾으면 String으로 보내줌
        return memberService.findId(joinDto.getName(),joinDto.getPhoneNumber(),null);
    }

    //비밀번호 찾기 페이지
    @GetMapping("/find/password")
    public String findPasswordPage(){
        return "member/findPassword";
    }

    //비밀번호 찾기
    @PostMapping("/find/password")
    @ResponseBody
    public String findPassword(JoinDto joinDto) throws MessagingException {
        //해당 이메일이 존재하지 않을 때
        if(memberService.duplicatedEmail(joinDto.getEmail())) return "500";

        //이메일이 존재하면 메일로 인증번호 보냄
        mailService.sendAuthCode(joinDto.getEmail());

        return "1000";
    }

    @PostMapping("/found/password")
    @ResponseBody
    public String foundPassword(JoinDto joinDto) throws AuthenticationException {

        //인증번호가 일치하지 않을 때
        if(!mailService.validationAuthCode(joinDto.getEmail(),joinDto.getAuthCode())) {
            return "500";
       }

        //일치하면 임시 비밀번호 발급
        String password  =  mailService.sendMailToChangePassword(joinDto.getEmail());

        //임시비밀번호로 DB 변경
        memberService.changePassword(password,joinDto.getEmail());

        return "1000";
    }
}
