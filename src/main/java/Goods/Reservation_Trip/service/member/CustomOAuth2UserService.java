package Goods.Reservation_Trip.service.member;

import Goods.Reservation_Trip.dto.member.req.CustomOauth2UserDetails;
import Goods.Reservation_Trip.dto.member.req.GoogleUserDetails;
import Goods.Reservation_Trip.dto.member.req.KakaoUserDetails;
import Goods.Reservation_Trip.dto.member.req.NaverUserDetails;
import Goods.Reservation_Trip.entity.Member;
import Goods.Reservation_Trip.enums.MemberRole;
import Goods.Reservation_Trip.inter.OAuth2UserInfo;
import Goods.Reservation_Trip.repository.MemberRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * OAuth 로그인 서비스
 */
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    private final HttpSession httpSession;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String provider = userRequest.getClientRegistration().getRegistrationId();

        OAuth2UserInfo oAuth2UserInfo = null;

        //구글로 로그인 한 경우
        if (provider.equals("google")) {
            oAuth2UserInfo = new GoogleUserDetails(oAuth2User.getAttributes());
        }

        //네이버로 로그인 한 경우
        else if(provider.equals("naver")){
            oAuth2UserInfo = new NaverUserDetails(oAuth2User.getAttributes());
        }

        else if (provider.equals("kakao")) {
            oAuth2UserInfo = new KakaoUserDetails(oAuth2User.getAttributes());
        }


        String providerId = oAuth2UserInfo.getProviderId();
        String loginId = oAuth2UserInfo.getEmail();
        String name = "";
        String birth = "";
        boolean gender = true;



        if(oAuth2UserInfo.getBirth() !=null && oAuth2UserInfo.getBirth().contains("-") ){
            birth = oAuth2UserInfo.getBirth().replace("-","");
        }

        if (oAuth2UserInfo.getGender().equals("F")) {
            gender = false;
        }

        //이름이 등록 안되어있을 경우
        if(oAuth2UserInfo.getName()==null || oAuth2UserInfo.getName().equals("")){
            name = provider + "회원";
        }
        else {
            name =oAuth2UserInfo.getName();
        }

        String phoneNumber = oAuth2UserInfo.getPhoneNumber();

        //네이버로 회원가입 시 휴대전화번호에 "-"가 포함되어 있어서 나중에 formatter 에서 오류날 수 있음
        if(phoneNumber!=null && phoneNumber.contains("-")){
            phoneNumber = phoneNumber.replace("-","");
        }

        Optional<Member> findMember = memberRepository.findByEmailAndProvider(loginId,provider);

        Member member;
        if (findMember.isEmpty()) {

            //소셜 로그인 계정이 아니며 이름과 휴대폰 번호가 중복인 회원이 있을 경우 가입 안됨
            Optional<Member> optionalMember = memberRepository.findByNameAndPhoneNumberAndProvider(name,phoneNumber,null);

            if(optionalMember.isPresent()) throw new RuntimeException("이름과 휴대전화번호가 중복되어 가입이 불가합니다.");

            member = Member.builder()
                    .name(name)
                    .birth(birth)
                    .gender(gender)
                    .email(loginId)
                    .provider(provider)
                    .providerId(providerId)
                    .role(MemberRole.MEMBER)
                    .phoneNumber(phoneNumber)
                    .privacyAgreement(true)
                    .termsAgreement(true)
                    .build();

            memberRepository.save(member);
        } else {
            member = findMember.get();
        }

        httpSession.setAttribute("memberId",member.getId());

        return new CustomOauth2UserDetails(member, oAuth2User.getAttributes());

    }
}
