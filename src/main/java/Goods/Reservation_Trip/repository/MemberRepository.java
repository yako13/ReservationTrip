package Goods.Reservation_Trip.repository;

import Goods.Reservation_Trip.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);

    Optional<Member> findByEmailAndProvider(String email,String provider);

    Optional<Member> findByNameAndPhoneNumber(String name, String phoneNumber);

    Optional<Member> findByNameAndPhoneNumberAndProvider(String name, String phoneNumber,String provider);

    Optional<Member> findByEmailAndPhoneNumber(String email, String phoneNumber);
}
