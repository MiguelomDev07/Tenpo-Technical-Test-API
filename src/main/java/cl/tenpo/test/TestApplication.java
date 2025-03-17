package cl.tenpo.test;

import cl.tenpo.test.components.MockExternalService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableCaching
@EnableAsync
public class TestApplication {

	public static void main(String[] args) {
		MockExternalService.activateMockServer();
		SpringApplication.run(TestApplication.class, args);
	}
}
