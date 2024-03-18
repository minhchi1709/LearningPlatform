package com.mchis.LearningPlatform.user;

import com.mchis.LearningPlatform.user.UserRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Data
@AllArgsConstructor
public class UserService implements UserDetailsService {
    private final static String USER_NOT_FOUND_MSG = "cannot find user with email %s";
    private final UserRepository repository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findByEmail(username)
                .orElseThrow(() ->
                    new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG, username))
                );
    }
}
