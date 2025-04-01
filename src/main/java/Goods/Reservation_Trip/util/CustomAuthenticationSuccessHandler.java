package Goods.Reservation_Trip.util;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        SavedRequest savedRequest = new HttpSessionRequestCache().getRequest(request, response);
//        String redirectUrl = "/";
        String redirectUrl = "/test10";
        String masterRedirectUrl = "/master/checkout/list";

        Collection<? extends GrantedAuthority> grantedAuthorities = authentication.getAuthorities();
        String role = "";
        for(GrantedAuthority authority : grantedAuthorities){
            role = authority.getAuthority();
        }

        if(role.equals("ROLE_MEMBER") && savedRequest != null){
            redirectUrl = savedRequest.getRedirectUrl();
            response.sendRedirect(redirectUrl);
        }
        else if(role.equals("ROLE_MEMBER")){
            response.sendRedirect(redirectUrl);
        }
        else if(role.equals("ROLE_ADMIN")){
            response.sendRedirect(masterRedirectUrl);
        }
        else if(role.equals("ROLE_CANCELLATION")){
            response.sendRedirect("/");
        }
    }
}
