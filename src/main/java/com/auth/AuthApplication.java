package com.auth;

import com.auth.model.Role;
import com.auth.model.User;
import com.auth.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;

@EnableJpaAuditing
@SpringBootApplication
public class AuthApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthApplication.class, args);
	}

	@Bean
	public CommandLineRunner run(UserService userService) {

		return args -> {

			userService.saveUser(new User(null, "andikaopp", "andikaopp@gmail.com", "123456", "085645890621", 0, null, new ArrayList<>()));
			userService.saveUser(new User(null, "julia", "julia@gmail.com", "123456", "085645890621", 0, null, new ArrayList<>()));

			userService.saveRole(new Role(null, "ROLE_ADMIN"));
			userService.saveRole(new Role(null, "ROLE_SUPER_ADMIN"));

			userService.addRoleToUser("andikaopp", "ROLE_SUPER_ADMIN");
			userService.addRoleToUser("julia", "ROLE_ADMIN");
		};
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
