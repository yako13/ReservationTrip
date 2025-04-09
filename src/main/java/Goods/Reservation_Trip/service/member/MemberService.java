package Goods.Reservation_Trip.service.member;

import Goods.Reservation_Trip.dto.member.req.EditDto;
import Goods.Reservation_Trip.dto.member.req.JoinDto;
import Goods.Reservation_Trip.dto.member.res.MemberResponseDto;
import Goods.Reservation_Trip.entity.Member;
import Goods.Reservation_Trip.enums.MemberRole;
import Goods.Reservation_Trip.repository.MemberRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
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
    public boolean duplicatedEmail(String email) {
        Optional<Member> member = memberRepository.findByEmail(email);

        //이메일이 중복되지 않았을 때 true 반환
        return member.isEmpty();
    }

    //회원가입
    public boolean join(JoinDto joinDto) {

        //이름과 휴대전화번호가 중복된 회원이 있는 경우 가입 불가
        if (memberRepository.findByNameAndPhoneNumber(joinDto.getName(), joinDto.getPhoneNumber()).isPresent())
            return false;

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
        return true;
    }

    //아이디찾기
    public String findId(String name, String phoneNumber, String provider) {
        Optional<Member> optionalMember = memberRepository.findByNameAndPhoneNumberAndProvider(name, phoneNumber, provider);
        if (optionalMember.isEmpty()) return null;
        Member member = optionalMember.get();

        String memberMail = member.getEmail();

        //메일 변환
        return memberMail.substring(0, memberMail.indexOf("@") - 3) + "***" + memberMail.substring(memberMail.indexOf("@"));
    }

    //임시비밀번호로 비밀번호 변경
    public void changePassword(String password, String email) {
        Optional<Member> optionalMember = memberRepository.findByEmailAndProvider(email, null);

        if (optionalMember.isEmpty()) throw new RuntimeException("존재하지 않는 회원");

        Member member = optionalMember.get();

        member.setPassword(passwordEncoder.encode(password));
    }

    //회원 정보 가져오기
    public MemberResponseDto getMember(HttpServletRequest request) {
        //세션에서 회원 PK 들고오기
        HttpSession session = request.getSession(false);

        if(session == null) return null;

        Long memberId = (Long) session.getAttribute("memberId");

        if (memberId == null) return null;

        //가져온 PK로 DB에서 회원 찾기
        Optional<Member> optionalMember = memberRepository.findById(memberId);

        if (optionalMember.isEmpty()) return null;

        Member member = optionalMember.get();

        //찾은 회원을 dto에 담아주기
        return MemberResponseDto.builder()
                .id(memberId)
                .email(member.getEmail())
                .name(member.getName())
                .provider(member.getProvider())
                .birth(member.getBirth())
                .gender(member.isGender())
                .phoneNumber(member.getPhoneNumber())
                .build();

    }

    //회원수정
    public boolean editMember(String email, EditDto editDto) {
        //가져온 이메일로 DB에서 회원찾기
        Optional<Member> optionalMember = memberRepository.findByEmail(email);

        if (optionalMember.isEmpty()) throw new RuntimeException("잘못된 접근");

        Member member = optionalMember.get();

        //비밀번호 = 바뀐 비밀번호
        String password = editDto.getPassword();

        //비밀번호 입력을 안했을 시
        if (editDto.getPassword().isEmpty()) {
            password = member.getPassword();
        }

        String name = editDto.getName();
        String birth = editDto.getBirth();
        String phoneNumber = editDto.getPhoneNumber();


        Optional<Member> aMember = memberRepository.findByNameAndPhoneNumber(name, phoneNumber);
        //이름이랑 휴대전화번호가 중복될 경우 수정 불가
        if (aMember.isPresent() && !aMember.get().getEmail().equals(member.getEmail())) return false;

        //회원정보 수정
        member.editMember(passwordEncoder.encode(password), name, birth, phoneNumber);

        //수정한 내용 저장
        memberRepository.save(member);

        return true;

    }

    public void memberWithdrawal(String email) {
        Optional<Member> optionalMember = memberRepository.findByEmail(email);

        if (optionalMember.isEmpty()) throw new RuntimeException("잘못된 접근");

        Member member = optionalMember.get();

        //회원탈퇴 : 권한 Member -> Cancellation 수정, 탈퇴 날짜 추가
        member.setWithdrawalAt();

    }

}
