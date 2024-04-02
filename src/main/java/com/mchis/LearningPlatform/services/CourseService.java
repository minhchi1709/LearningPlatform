package com.mchis.LearningPlatform.services;

import com.mchis.LearningPlatform.entities.Course;
import com.mchis.LearningPlatform.repositories.CourseRepository;
import com.mchis.LearningPlatform.repositories.UserRepository;
import com.mchis.LearningPlatform.entities.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Data
@Service
public class CourseService {
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("No user with username = " + username));
    }

    public Course getCourseById(Integer id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("No course with id = " + id));
    }

    public void register(Integer id, UserDetails currentUser) {
        User user = getUserByUsername(currentUser.getUsername());
        Course course = getCourseById(id);
        List<User> students = course.getStudents();
        students.add(user);
        course.setStudents(students);
        List<Course> courses = user.getStudyingCourses();
        courses.add(course);
        user.setStudyingCourses(courses);
        userRepository.save(user);
        courseRepository.save(course);
    }

    public void deregister(Integer id, UserDetails currentUser) {
        User user = getUserByUsername(currentUser.getUsername());
        Course course = getCourseById(id);
        List<User> students = course.getStudents();
        students.remove(user);
        course.setStudents(students);
        List<Course> courses = user.getStudyingCourses();
        courses.remove(course);
        user.setStudyingCourses(courses);
        userRepository.save(user);
        courseRepository.save(course);
    }

    public List<Course> getAllNotTeachingAndNotStudyingCourses(UserDetails currentUser) {
        User user = getUserByUsername(currentUser.getUsername());
        List<Course> studyingCourses = user.getStudyingCourses();
        List<Course> teachingCourses = user.getTeachingCourses();
        return getAllCourses()
                .stream()
                .filter(course -> !studyingCourses.contains(course) && !teachingCourses.contains(course))
                .collect(Collectors.toList());
    }

    public void createNewCourse(Course course, UserDetails currentUser) {
        User user = getUserByUsername(currentUser.getUsername());
        Course newCourse = Course.builder()
                .name(course.getName())
                .description(course.getDescription())
                .teacher(user)
                .students(new ArrayList<>()).build();
        courseRepository.save(newCourse);
        List<Course> courses = user.getTeachingCourses();
        courses.add(newCourse);
        user.setTeachingCourses(courses);
        userRepository.save(user);
    }

    public void edit(Course newCourse, Integer id) {
        Course course = getCourseById(id);
        course.setName(newCourse.getName());
        course.setDescription(newCourse.getDescription());
        courseRepository.save(course);
    }
}
