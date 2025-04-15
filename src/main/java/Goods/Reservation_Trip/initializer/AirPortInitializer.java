package Goods.Reservation_Trip.initializer;

import Goods.Reservation_Trip.entity.Airport;
import Goods.Reservation_Trip.repository.aPackage.AirportRepository;
import Goods.Reservation_Trip.repository.aPackage.PackageCategoryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Order(2)
@Transactional
public class AirPortInitializer implements CommandLineRunner {

    private final AirportRepository airportRepository;

    private final PackageCategoryRepository packageCategoryRepository;

    @Override
    public void run(String... Args) throws Exception {

        // 국내 국제 공항
        packageCategoryRepository.findByName("김포").ifPresent(gimpo -> {
            if (!airportRepository.existsByCode("GMP")) {
                airportRepository.save(new Airport(null, "GMP", "김포국제공항", List.of(gimpo)));
            }
        });
        packageCategoryRepository.findByName("인천").ifPresent(incheon -> {
            if (!airportRepository.existsByCode("ICN")) {
                airportRepository.save(new Airport(null, "ICN", "인천국제공항", List.of(incheon)));
            }
        });
        packageCategoryRepository.findByName("대구").ifPresent(daegu->{
        if (!airportRepository.existsByCode("TAE")) {
            airportRepository.save(new Airport(null, "TAE", "대구국제공항", List.of(daegu)));
        }
        });
        packageCategoryRepository.findByName("부산").ifPresent(busan->{
        if (!airportRepository.existsByCode("PUS")) {
            airportRepository.save(new Airport(null, "PUS", "부산국제공항", List.of(busan)));
        }
        });

        // 해외 국제 공항
        // 카테고리에 해당 이름이 있으면 연결
        packageCategoryRepository.findByName("도쿄").ifPresent(tokyo -> {
            if (!airportRepository.existsByCode("NRT")) {
                airportRepository.save(new Airport(null, "NRT", "나리타국제공항", List.of(tokyo)));
            }
            if (!airportRepository.existsByCode("HND")) {
                airportRepository.save(new Airport(null, "HND", "하네다국제공항", List.of(tokyo)));
            }
        });

        packageCategoryRepository.findByName("오사카").ifPresent(osaka -> {
            if (!airportRepository.existsByCode("KIX")) {
                airportRepository.save(new Airport(null, "KIX", "간사이국제공항", List.of(osaka)));
            }
        });

        packageCategoryRepository.findByName("삿포로").ifPresent(sapporo -> {
            if (!airportRepository.existsByCode("CTS")) { // 신치토세 공항
                airportRepository.save(new Airport(null, "CTS", "신치토세 공항", List.of(sapporo)));
            }
        });

        packageCategoryRepository.findByName("후라노").ifPresent(furano -> {
            if (!airportRepository.existsByCode("AKJ")) { // 아사히카와 공항
                airportRepository.save(new Airport(null, "AKJ", "아사히카와 공항", List.of(furano)));
            }
        });

        packageCategoryRepository.findByName("후쿠오카").ifPresent(fukuoka -> {
            if (!airportRepository.existsByCode("FUK")) {
                airportRepository.save(new Airport(null, "FUK", "후쿠오카 공항", List.of(fukuoka)));
            }
        });

        packageCategoryRepository.findByName("나가사키").ifPresent(nagasaki -> {
            if (!airportRepository.existsByCode("NGS")) {
                airportRepository.save(new Airport(null, "NGS", "나가사키 공항", List.of(nagasaki)));
            }
        });

        packageCategoryRepository.findByName("뱃푸").ifPresent(beppu -> {
            if (!airportRepository.existsByCode("OIT")) { // 오이타 공항
                airportRepository.save(new Airport(null, "OIT", "오이타 공항", List.of(beppu)));
            }
        });

        packageCategoryRepository.findByName("베이징").ifPresent(beijing -> {
            if (!airportRepository.existsByCode("PEK")) {
                airportRepository.save(new Airport(null, "PEK", "베이징 수도 국제공항", List.of(beijing)));
            }
            if (!airportRepository.existsByCode("PKX")) {
                airportRepository.save(new Airport(null, "PKX", "베이징 다싱 국제공항", List.of(beijing)));
            }
        });

        packageCategoryRepository.findByName("텐진").ifPresent(tianjin -> {
            if (!airportRepository.existsByCode("TSN")) {
                airportRepository.save(new Airport(null, "TSN", "텐진 빈하이 국제공항", List.of(tianjin)));
            }
        });

        packageCategoryRepository.findByName("상하이").ifPresent(shanghai -> {
            if (!airportRepository.existsByCode("PVG")) {
                airportRepository.save(new Airport(null, "PVG", "푸동 국제공항", List.of(shanghai)));
            }
            if (!airportRepository.existsByCode("SHA")) {
                airportRepository.save(new Airport(null, "SHA", "훙차오 국제공항", List.of(shanghai)));
            }
        });

        packageCategoryRepository.findByName("항저우").ifPresent(hangzhou -> {
            if (!airportRepository.existsByCode("HGH")) {
                airportRepository.save(new Airport(null, "HGH", "항저우 샤오산 국제공항", List.of(hangzhou)));
            }
        });

        packageCategoryRepository.findByName("하얼빈").ifPresent(harbin -> {
            if (!airportRepository.existsByCode("HRB")) {
                airportRepository.save(new Airport(null, "HRB", "하얼빈 타이핑 국제공항", List.of(harbin)));
            }
        });

        // 미국 서부 지역 공항 추가
        packageCategoryRepository.findByName("로스앤젤레스").ifPresent(losAngeles -> {
            if (!airportRepository.existsByCode("LAX")) {
                airportRepository.save(new Airport(null, "LAX", "로스앤젤레스 국제공항", List.of(losAngeles)));
            }
            if (!airportRepository.existsByCode("BUR")) {
                airportRepository.save(new Airport(null, "BUR", "밥 호프 공항", List.of(losAngeles)));
            }
        });

        packageCategoryRepository.findByName("라스베가스").ifPresent(lasVegas -> {
            if (!airportRepository.existsByCode("LAS")) {
                airportRepository.save(new Airport(null, "LAS", "매캐런 국제공항", List.of(lasVegas)));
            }
        });

        packageCategoryRepository.findByName("샌프란시스코").ifPresent(sanFrancisco -> {
            if (!airportRepository.existsByCode("SFO")) {
                airportRepository.save(new Airport(null, "SFO", "샌프란시스코 국제공항", List.of(sanFrancisco)));
            }
            if (!airportRepository.existsByCode("OAK")) {
                airportRepository.save(new Airport(null, "OAK", "오클랜드 국제공항", List.of(sanFrancisco)));
            }
        });

        // 미국 동부 지역 공항 추가
        packageCategoryRepository.findByName("뉴욕").ifPresent(newYork -> {
            if (!airportRepository.existsByCode("JFK")) {
                airportRepository.save(new Airport(null, "JFK", "존 F. 케네디 국제공항", List.of(newYork)));
            }
            if (!airportRepository.existsByCode("LGA")) {
                airportRepository.save(new Airport(null, "LGA", "라과디아 공항", List.of(newYork)));
            }
            if (!airportRepository.existsByCode("EWR")) {
                airportRepository.save(new Airport(null, "EWR", "뉴어크 리버티 국제공항", List.of(newYork)));
            }
        });

        packageCategoryRepository.findByName("워싱턴 D.C.").ifPresent(washingtonDC -> {
            if (!airportRepository.existsByCode("IAD")) {
                airportRepository.save(new Airport(null, "IAD", "덜레스 국제공항", List.of(washingtonDC)));
            }
            if (!airportRepository.existsByCode("DCA")) {
                airportRepository.save(new Airport(null, "DCA", "레이건 국제공항", List.of(washingtonDC)));
            }
        });

        // 미국 중부 지역 공항 추가
        packageCategoryRepository.findByName("시카고").ifPresent(chicago -> {
            if (!airportRepository.existsByCode("ORD")) {
                airportRepository.save(new Airport(null, "ORD", "오헤어 국제공항", List.of(chicago)));
            }
            if (!airportRepository.existsByCode("MDW")) {
                airportRepository.save(new Airport(null, "MDW", "미드웨이 국제공항", List.of(chicago)));
            }
        });

        // 미국 남부 지역 공항 추가
        packageCategoryRepository.findByName("댈러스").ifPresent(dallas -> {
            if (!airportRepository.existsByCode("DFW")) {
                airportRepository.save(new Airport(null, "DFW", "댈러스 포트워스 국제공항", List.of(dallas)));
            }
        });

        packageCategoryRepository.findByName("마이애미").ifPresent(miami -> {
            if (!airportRepository.existsByCode("MIA")) {
                airportRepository.save(new Airport(null, "MIA", "마이애미 국제공항", List.of(miami)));
            }
        });

        // 하와이 지역 공항 추가
        packageCategoryRepository.findByName("호놀룰루").ifPresent(honolulu -> {
            if (!airportRepository.existsByCode("HNL")) {
                airportRepository.save(new Airport(null, "HNL", "다니엘 K. 이노우에 국제공항", List.of(honolulu)));
            }
        });
    }
}
