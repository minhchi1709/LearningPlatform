package com.mchis.LearningPlatform;

import com.github.javafaker.Faker;
import com.mchis.LearningPlatform.entities.Course;
import com.mchis.LearningPlatform.repositories.CourseRepository;
import com.mchis.LearningPlatform.repositories.UserRepository;
import com.mchis.LearningPlatform.entities.User;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

import static com.mchis.LearningPlatform.enums.Role.USER;

@SpringBootApplication
public class LearningPlatformApplication {

	public static void main(String[] args) {
		SpringApplication.run(LearningPlatformApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(
		CourseRepository courseRepository,
		UserRepository userRepository,
		PasswordEncoder passwordEncoder
	) {
		return args -> {
			User teacher = User.builder()
					.firstname("Minh Chi")
					.lastname("Diep")
					.username("diepminhchi")
					.email("diepminhchi@mail.com")
					.password(passwordEncoder.encode("123"))
					.role(USER)
					.locked(false)
					.enabled(true)
					.build();
			userRepository.save(teacher);
			Course cs = Course.builder()
					.name("Computer Science")
					.description("CS50")
					.teacher(teacher)
					.build();
			courseRepository.save(cs);
			List<User> students = new ArrayList<>();
			for (int i = 0 ; i < 50 ; i++) {
				Faker faker = new Faker();
				List<Course> courses = new ArrayList<>();
				courses.add(cs);
				User student = User.builder()
						.firstname(faker.name().firstName())
						.lastname(faker.name().lastName())
						.username(faker.name().username())
						.email(faker.name().fullName() + i + "@mail.com")
						.role(USER)
						.enabled(true)
						.locked(false)
						.studyingCourses(courses)
						.password(passwordEncoder.encode(faker.name().fullName())).build();
				userRepository.save(student);
				students.add(student);
			}
			cs.setStudents(students);
			courseRepository.save(cs);
		};
	}

	@Bean
	public CommandLineRunner commandLineRunnerCourses(
			CourseRepository courseRepository,
			UserRepository userRepository,
			PasswordEncoder passwordEncoder
	) {
		return args -> {
			String name = "Java ";
			for (int i = 0 ; i < 10 ; i++) {
				Faker faker = new Faker();
				List<Course> courses = new ArrayList<>();
				User teacher = User.builder()
						.firstname(faker.name().firstName())
						.lastname(faker.name().lastName())
						.username(faker.name().username())
						.email(faker.name().fullName() + i + "@gmail.com")
						.role(USER)
						.enabled(true)
						.locked(false)
						.studyingCourses(new ArrayList<>())
						.password(passwordEncoder.encode(faker.name().fullName())).build();
				userRepository.save(teacher);
				Course course = Course.builder()
						.name(name + i)
						.description(name + "level " + i)
						.teacher(teacher).build();
				courseRepository.save(course);
				courses.add(course);
				teacher.setTeachingCourses(courses);
				userRepository.save(teacher);
			}
		};
	}
}
