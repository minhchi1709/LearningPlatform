package com.mchis.LearningPlatform;

import com.mchis.LearningPlatform.authentication.AuthenticationService;
import com.mchis.LearningPlatform.authentication.RegisterRequest;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import static com.mchis.LearningPlatform.user.Role.ADMIN;

@SpringBootApplication
public class LearningPlatformApplication {

	public static void main(String[] args) {
		SpringApplication.run(LearningPlatformApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(
			AuthenticationService service
	) {
		return args -> {
			var admin = RegisterRequest.builder()
					.firstname("Admin")
					.lastname("Admin")
					.email("admin@mail.com")
					.password("password")
					.role(ADMIN)
					.build();
			System.out.println("Admin token: " + service.register(admin).getAccessToken());

		};
	}
}
