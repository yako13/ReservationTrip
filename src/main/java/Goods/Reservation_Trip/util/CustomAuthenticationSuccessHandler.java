package Goods.Reservation_Trip.util;

import Goods.Reservation_Trip.dto.member.req.CustomOauth2UserDetails;
import Goods.Reservation_Trip.entity.Member;
import Goods.Reservation_Trip.repository.MemberRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final MemberRepository memberRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        SavedRequest savedRequest = new HttpSessionRequestCache().getRequest(request, response);
        String redirectUrl = "/";

        String masterRedirectUrl = "/admin/reservation/list";

        CustomOauth2UserDetails oAuth2User = (CustomOauth2UserDetails) authentication.getPrincipal();
        Member member = oAuth2User.getMember();


        Collection<? extends GrantedAuthority> grantedAuthorities = authentication.getAuthorities();

        String role = "";
        for (GrantedAuthority authority : grantedAuthorities) {
            role = authority.getAuthority();
        }


        if (role.equals("ROLE_MEMBER") && savedRequest != null) {
            if (checkBirth(member)) {
                redirectUrl = savedRequest.getRedirectUrl();
                response.sendRedirect(redirectUrl);
            } else response.sendRedirect("/member/edit");
        } else if (role.equals("ROLE_MEMBER")) {
            if (checkBirth(member)) {
                response.sendRedirect(redirectUrl);
            } else response.sendRedirect("/member/edit");
        } else if (role.equals("ROLE_ADMIN")) {
            response.sendRedirect(masterRedirectUrl);
        } else if (role.equals("ROLE_CANCELLATION")) {
            response.sendRedirect("/");
        }
    }

    //생년월일이나 휴대전화번호가 비어있는지 체크
    public boolean checkBirth(Member member) {
        return member.getBirth() != null && member.getPhoneNumber() != null;
    }
}
