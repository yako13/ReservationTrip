package Goods.Reservation_Trip.service.HanService;

import Goods.Reservation_Trip.dto.member.res.MemberResponseDto;
import Goods.Reservation_Trip.entity.Member;
import Goods.Reservation_Trip.repository.MemberRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class HanMemberService {

    private final MemberRepository memberRepository;


    //회원 정보 가져오기 (api에서 세션 체크하기위해 )
    public MemberResponseDto getMember(HttpServletRequest request) {
        //세션에서 회원 PK 들고오기
        HttpSession session = request.getSession(false);

        if (session == null) {
            return null;
        }

        Long memberId = (Long) session.getAttribute("memberId");

        if (memberId == null) {
            return null;
        }

        //가져온 PK로 DB에서 회원 찾기
        Member memberEntity = memberRepository.findById(memberId).orElse(null);

        if (memberEntity == null) {
            return null;
        } ;


        //찾은 회원을 dto에 담아주기
        return MemberResponseDto.builder()
                .id(memberId)
                .email(memberEntity.getEmail())
                .name(memberEntity.getName())
                .provider(memberEntity.getProvider())
                .birth(memberEntity.getBirth())
                .gender(memberEntity.isGender())
                .phoneNumber(memberEntity.getPhoneNumber())
                .build();

    }


}
