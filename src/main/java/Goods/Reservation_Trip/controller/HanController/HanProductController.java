package Goods.Reservation_Trip.controller.HanController;

import Goods.Reservation_Trip.dto.member.res.MemberResponseDto;
import Goods.Reservation_Trip.entity.Member;
import Goods.Reservation_Trip.entity.Package;
import Goods.Reservation_Trip.repository.HanPart.HanDibRepository;
import Goods.Reservation_Trip.repository.MemberRepository;
import Goods.Reservation_Trip.repository.aPackage.PackageRepository;
import Goods.Reservation_Trip.service.HanService.HanMemberService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HanProductController {

    private final HanMemberService hanMemberService;
    private final MemberRepository memberRepository;
    private final HanDibRepository hanDibRepository;
    private final PackageRepository packageRepository;

    //패키지 상세 페이지
    @GetMapping("package/details")
    public String packageDetailsGo(Model model, HttpServletRequest request) {

        MemberResponseDto memberResponseDto = hanMemberService.getMember(request);

        boolean isLiked = false;

        if (memberResponseDto != null) {

            Member member = memberRepository.findById(memberResponseDto.getId()).orElse(null);

            if (member != null) {

                Package aPackage = packageRepository.findById(1L).orElse(null);

                isLiked = hanDibRepository.existsByMemberAndPackageEntity(member, aPackage);
            }


        }

        model.addAttribute("isLiked", isLiked); // 하트 상태 보내기


        return "package/packageDetails";
    }


}
