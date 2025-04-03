package Goods.Reservation_Trip;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class ReservationTripApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReservationTripApplication.class, args);
	}

}
