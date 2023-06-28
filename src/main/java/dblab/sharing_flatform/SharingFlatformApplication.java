package dblab.sharing_flatform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SharingFlatformApplication {
	public static void main(String[] args) {
		SpringApplication.run(SharingFlatformApplication.class, args);
	}

}
