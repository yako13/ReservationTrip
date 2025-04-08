package Goods.Reservation_Trip.repository.HanPart;

import Goods.Reservation_Trip.entity.Dib;
import Goods.Reservation_Trip.entity.Member;
import Goods.Reservation_Trip.entity.Package;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface HanDibRepository extends JpaRepository<Dib,Long> {

    //세션의 이메일 정보와 찜 pk로 찜 엔티티 찾아옴
    //찜 삭제할려는 사용자가 찜 등록한 사람과 같은지 검증을 위해
    Optional<Dib> findByIdAndMemberEmail(Long dibId, String email);

    //세션의 이메일 정보와 찜 pk로 찜 엔티티 전체를 찾아옴
    //찜 삭제할려는 사용자가 찜 등록한 사람과 같은지 검증을 위해
    List<Dib> findAllByMemberEmail(String email);


    //찜 추가  (이미 찜이 있는지 검사)
    boolean existsByMemberAndPackageEntity(Member member, Package aPackage);

    //찜 취소
    Optional<Dib> findByMemberAndPackageEntity(Member member,Package aPackage);

}
