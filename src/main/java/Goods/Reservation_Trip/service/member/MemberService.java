package Goods.Reservation_Trip.service.member;

import Goods.Reservation_Trip.dto.member.req.JoinDto;
import Goods.Reservation_Trip.entity.Member;
import Goods.Reservation_Trip.enums.MemberRole;
import Goods.Reservation_Trip.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;

    //이메일 중복 확인
    public boolean duplicatedEmail(String email){
        Optional<Member> member = memberRepository.findByEmail(email);

        //이메일이 중복되지 않았을 때 true 반환
        return member.isEmpty();
    }

    //회원가입
    public void join(JoinDto joinDto){
        Member member = Member.builder()
                .email(joinDto.getEmail())
                .password(passwordEncoder.encode(joinDto.getPassword()))
                .name(joinDto.getName())
                .birth(joinDto.getBirth())
                .gender(joinDto.isGender())
                .phoneNumber(joinDto.getPhoneNumber())
                .role(MemberRole.MEMBER)
                .termsAgreement(true)
                .privacyAgreement(true)
                .build();

        memberRepository.save(member);
    }

    //아이디찾기
    public String findId(String name , String phoneNumber,String provider){
      Optional<Member> optionalMember =  memberRepository.findByNameAndPhoneNumberAndProvider(name, phoneNumber, provider);
      if(optionalMember.isEmpty()) return null;
      Member member = optionalMember.get();

      String memberMail = member.getEmail();

      //메일 변환
      String newMail = memberMail.substring(0,memberMail.indexOf("@")-3) + "***" + memberMail.substring(memberMail.indexOf("@"));

      return newMail;
    }

}
