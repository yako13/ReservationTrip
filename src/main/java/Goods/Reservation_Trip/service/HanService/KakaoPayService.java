package Goods.Reservation_Trip.service.HanService;

import Goods.Reservation_Trip.dto.HanDto.KakaoPay;
import Goods.Reservation_Trip.dto.member.res.MemberResponseDto;
import Goods.Reservation_Trip.entity.Package;
import Goods.Reservation_Trip.repository.HanPart.HanReservationRepository;
import Goods.Reservation_Trip.repository.aPackage.PackageRepository;
import Goods.Reservation_Trip.service.member.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoPayService {

    @Value("${kakaopay.admin-key}")
    private String adminKey; // "KakaoAK {실제 Admin 키}" 포함된 전체 문자열

    private final RestTemplate restTemplate = new RestTemplate();
    private final HanReservationRepository hanReservationRepository;
    private final PackageRepository packageRepository;
    private final MemberService memberService;

    // 카카오페이 결제 준비 요청
    public String kakaoPayReady(Long packagePk, HttpSession session, HttpServletRequest sessionRequest, int totalPrice) {
        HttpHeaders headers = new HttpHeaders();

        //패키지 pk에서 패키지 엔티티 찾기
        Package packageEntity = packageRepository.findById(packagePk).orElse(null);

        //세션에서 맴버 정보 추출
        MemberResponseDto memberEntity = memberService.getMember(sessionRequest);

        //맴버 pk추출
        Long memberPk = memberEntity.getId();

        //파트너 유저아이디 임시 생성 (user + 맴버 pk)
        String partnerUserId = "user" + memberPk;

        //카카오 api 보내기
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "SECRET_KEY " + adminKey); // KakaoAK 포함된 문자열

        Map<String, Object> params = new HashMap<>();
        //테스트 코드
        params.put("cid", "TC0ONETIME");
        //회사명
        params.put("partner_order_id", "NyangManTravel");
        //유저 아이디 (이름이나 아이디를 보내면 안되니  (user + 맴버 pk) 로 보냄)
        params.put("partner_user_id", partnerUserId);
        //상품명
        params.put("item_name", packageEntity.getPackageName());
        params.put("quantity", 1);
        //총 금액
        params.put("total_amount", totalPrice);
//        params.put("total_amount", totalPrice);
        //부과세 (안보내면 알아서 계산해줌 총금액 - 비과세)
//        params.put("vat_amount", 200);
        //비과세
        params.put("tax_free_amount", 0);
        //성공시 url
        params.put("approval_url", "http://192.168.106.89:8080/pay/approve");
        //실패시 url
        params.put("fail_url", "http://192.168.106.89:8080/pay/fail");
        //취소 시 url
        params.put("cancel_url", "http://192.168.106.89:8080/pay/cancel");

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(params, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(
                "https://open-api.kakaopay.com/online/v1/payment/ready",
                request,
                Map.class
        );

        Map<String, Object> body = response.getBody();
        String tid = (String) body.get("tid");

        session.setAttribute("tid", tid); //  세션에 저장

        log.info("카카오 페이 결제 준비 요청 성공");

        return (String) body.get("next_redirect_pc_url");
    }

    // 카카오페이 결제 승인 요청
    public KakaoPay approvePayment(String pgToken, HttpSession session, HttpServletRequest sessionRequest) {

        //세션에서 맴버 정보 추출
        MemberResponseDto memberEntity = memberService.getMember(sessionRequest);

        //맴버 pk추출
        Long memberPk = memberEntity.getId();

        //파트너 유저아이디 임시 생성 (user + 맴버 pk)
        String partnerUserId = "user" + memberPk;

        String tid = (String) session.getAttribute("tid");
        if (tid == null) throw new IllegalStateException("세션에 tid 없음");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "SECRET_KEY " + adminKey);

        Map<String, Object> params = new HashMap<>();
        params.put("cid", "TC0ONETIME");
        params.put("tid", tid);
        params.put("partner_order_id", "NyangManTravel");
        params.put("partner_user_id", partnerUserId);
        params.put("pg_token", pgToken);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(params, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(
                "https://open-api.kakaopay.com/online/v1/payment/approve",
                request,
                Map.class
        );

        Map<String, Object> body = response.getBody();

        //  응답값 추출
        String aid = (String) body.get("aid");
        String paymentMethod = (String) body.get("payment_method_type");
        String approvedAtStr = (String) body.get("approved_at");

        Map<String, Object> amountMap = (Map<String, Object>) body.get("amount");
        int totalAmount = (int) amountMap.get("total");

        Map<String, Object> cardInfo = (Map<String, Object>) body.get("card_info");

        //  KakaoPay 객체에 담기
        KakaoPay kakaoPay = new KakaoPay();
        kakaoPay.setAid(aid);
        kakaoPay.setTid(tid);
        kakaoPay.setAmount(totalAmount);
        kakaoPay.setApprovedAt(LocalDateTime.parse(approvedAtStr));
        kakaoPay.setPayType(paymentMethod);

        if (cardInfo != null) {
            kakaoPay.setPurCorp((String) cardInfo.get("kakaopay_purchase_corp"));
            kakaoPay.setPurCorpCode((String) cardInfo.get("kakaopay_purchase_corp_code"));
            kakaoPay.setIssuerCorp((String) cardInfo.get("kakaopay_issuer_corp"));
            kakaoPay.setApproved((String) cardInfo.get("approve_id"));
        }

        log.info("카카오 페이 결제 성공");


        return kakaoPay;
    }

//    public List<Payment> getAllPayments() {
//        return hanReservationRepository.findAll();
//    }
}




