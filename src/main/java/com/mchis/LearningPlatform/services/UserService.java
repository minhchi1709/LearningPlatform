package com.mchis.LearningPlatform.services;

import com.mchis.LearningPlatform.dto.UserDto;
import com.mchis.LearningPlatform.entities.Course;
import com.mchis.LearningPlatform.repositories.UserRepository;
import com.mchis.LearningPlatform.entities.User;
import lombok.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Data
@AllArgsConstructor
@Builder
public class UserService implements UserDetailsService {
    private final static String USER_NOT_FOUND_MSG = "cannot find user with email %s";
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() ->
                    new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG, username))
                );
    }

    public List<Course> getAllStudyingCourses(UserDetails currentUser) {
        User user = userRepository.findByUsername(currentUser.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("No user with username = " + currentUser.getUsername()));
        return user.getStudyingCourses();
    }

    public List<Course> getAllTeachingCourses(UserDetails currentUser) {
        User user = userRepository.findByUsername(currentUser.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("No user with username = " + currentUser.getUsername()));
        return user.getTeachingCourses();
    }

    public User loadUserByCurrentUser(UserDetails currentUser) {
        return userRepository.findByUsername(currentUser.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("No user with username = " + currentUser.getUsername()));
    }

    public void editUser(UserDetails currentUser, UserDto newUser) {
        User user = loadUserByCurrentUser(currentUser);
        user.setUsername(newUser.username());
        user.setFirstname(newUser.firstname());
        user.setLastname(newUser.lastname());
        user.setEmail(newUser.email());
        userRepository.save(user);
    }

    public UserDto convertUserToUserDto(User user) {
        return UserDto.builder()
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .username(user.getUsername())
                .email(user.getEmail())
                .build();
    }

}
