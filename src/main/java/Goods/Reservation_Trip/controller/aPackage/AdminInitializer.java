package Goods.Reservation_Trip.controller.aPackage;

import Goods.Reservation_Trip.entity.Member;
import Goods.Reservation_Trip.enums.MemberRole;
import Goods.Reservation_Trip.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminInitializer implements CommandLineRunner {

    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;

    @Value("${admin.account1}")
    private String adminAccount1;

    @Value("${admin.password1}")
    private String adminPassword1;

    public void run(String... args) {


        if (memberRepository.count() == 0) {
            String encodedPassword1 = passwordEncoder.encode(adminPassword1);
            Member member1 = Member.builder()
                    .email(adminAccount1)
                    .password(encodedPassword1)
                    .name("Admin User1")
                    .role(MemberRole.ADMIN)
                    .termsAgreement(true)
                    .privacyAgreement(true)
                    .build();

            memberRepository.save(member1);
        }
    }
}
