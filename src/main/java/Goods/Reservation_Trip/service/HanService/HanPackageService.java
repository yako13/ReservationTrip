package Goods.Reservation_Trip.service.HanService;

import Goods.Reservation_Trip.dto.member.res.MemberResponseDto;
import Goods.Reservation_Trip.entity.Package;
import Goods.Reservation_Trip.repository.HanPart.HanReservationDetailsRepository;
import Goods.Reservation_Trip.repository.HanPart.HanReservationRepository;
import Goods.Reservation_Trip.repository.MemberRepository;
import Goods.Reservation_Trip.repository.aPackage.PackageRepository;
import Goods.Reservation_Trip.service.member.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HanPackageService {

    private final MemberRepository memberRepository;
    private final PackageRepository packageRepository;
    private final MemberService memberService;
    private final HanReservationRepository hanReservationRepository;
    private final HanReservationDetailsRepository hanReservationDetailsRepository;

//    public MemberResponseDto productDetailGo(HttpServletRequest request, Long id) {
//
//        Package PackageEntity = packageRepository.findById(id).orElse(null);
//
//        PackageEntity.getPackageName()
//
//    }


}
