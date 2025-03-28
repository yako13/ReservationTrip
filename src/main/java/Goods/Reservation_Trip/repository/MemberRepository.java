package Goods.Reservation_Trip.repository;

import Goods.Reservation_Trip.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByUserId(String userId);

    Optional<Member> findByUserIdAndProvider(String userId,String provider);

    Optional<Member> findByNameAndPhoneNumber(String name, String phoneNumber);

    Optional<Member> findByNameAndPhoneNumberAndProvider(String name, String phoneNumber,String provider);

    Optional<Member> findByUserIdAndPhoneNumber(String userId, String phoneNumber);
}
