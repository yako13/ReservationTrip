package Goods.Reservation_Trip;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableJpaAuditing
@EnableScheduling
@SpringBootApplication
public class ReservationTripApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReservationTripApplication.class, args);
	}

}
