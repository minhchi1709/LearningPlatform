package com.mchis.LearningPlatform.services;


import com.mchis.LearningPlatform.dto.UserChangePasswordDto;
import com.mchis.LearningPlatform.dto.UserPasswordDto;
import com.mchis.LearningPlatform.entities.EmailDetails;
import com.mchis.LearningPlatform.exceptions.OldPasswordNotCorrectException;
import com.mchis.LearningPlatform.exceptions.TokenExpiredException;
import com.mchis.LearningPlatform.exceptions.UserAlreadyExistException;
import com.mchis.LearningPlatform.entities.ConfirmationToken;
import com.mchis.LearningPlatform.repositories.ConfirmationTokenRepository;
import com.mchis.LearningPlatform.enums.Role;
import com.mchis.LearningPlatform.entities.User;
import com.mchis.LearningPlatform.repositories.UserRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@AllArgsConstructor
@Data
@Service
public class AuthenticationService {
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final EmailServiceImpl emailService;
    public void registerUser(User registerUser) throws UserAlreadyExistException {
        System.out.println("Registration for user with username:" + registerUser.getUsername());
        Optional<User> existingEmail = userRepository.findByEmail(registerUser.getEmail());
        if (existingEmail.isPresent()){
            Optional<ConfirmationToken> optionalConfirmationToken = confirmationTokenRepository.findByUser(existingEmail.get());
            // If there is a token linked to this user, we need to delete it and then create new one
            if (optionalConfirmationToken.isPresent()) {
                confirmationTokenRepository.delete(optionalConfirmationToken.get());
            } else {
                throw new UserAlreadyExistException();
            }
        }
        Optional<User> existingUsername = userRepository.findByUsername(registerUser.getUsername());
        if (existingUsername.isPresent()){
            Optional<ConfirmationToken> optionalConfirmationToken = confirmationTokenRepository.findByUser(existingUsername.get());
            // If there is a token linked to this user, we need to delete it and then create new one
            if (optionalConfirmationToken.isPresent()) {
                confirmationTokenRepository.delete(optionalConfirmationToken.get());
            } else {
                throw new UserAlreadyExistException();
            }
        }
        User user = userRepository.save(User.builder()
                .firstname(registerUser.getFirstname())
                .lastname(registerUser.getLastname())
                .username(registerUser.getUsername())
                .email(registerUser.getEmail())
                .password(passwordEncoder.encode(registerUser.getPassword()))
                .locked(false)
                .enabled(false)
                .role(Role.USER)
                .build());
        ConfirmationToken confirmationToken = new ConfirmationToken(user);
        confirmationTokenRepository.save(confirmationToken);

        emailService.sendSimpleMail(
                EmailDetails.builder()
                        .recipient(user.getEmail())
                        .subject("Complete Registration")
                        .msgBody("To confirm your account, please click here : "
                                +"http://localhost:8080/auth/confirm-account?token="+confirmationToken.getConfirmationToken())
                        .build()
        );
    }

    public void confirmUserAccount(String confirmationToken) throws TokenExpiredException, UsernameNotFoundException {
        Optional<ConfirmationToken> optionalConfirmationToken = confirmationTokenRepository.findByConfirmationToken(confirmationToken);
        if(optionalConfirmationToken.isPresent()) {
            ConfirmationToken token = optionalConfirmationToken.get();
            confirmationTokenRepository.delete(token);
            if (LocalDateTime.now().isBefore(token.getExpiresAt())) {
                Optional<User> optionalUser = userRepository.findByEmail(token.getUser().getEmail());
                if (optionalUser.isPresent()) {
                    User user = optionalUser.get();
                    user.setEnabled(true);
                    userRepository.save(user);
                    return;
                } else {
                    throw new UsernameNotFoundException("Username not found");
                }
            }
        }
        throw new TokenExpiredException();
    }

    public void sendVerificationEmailForGenerateNewPassword(String email) throws UsernameNotFoundException {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) {
            throw new UsernameNotFoundException("No user with this email");
        }
        User user = optionalUser.get();
        ConfirmationToken confirmationToken = new ConfirmationToken(user);
        confirmationTokenRepository.save(confirmationToken);
        emailService.sendSimpleMail(
                EmailDetails.builder()
                        .recipient(user.getEmail())
                        .subject("New Password")
                        .msgBody("To set your new password, please click here : "
                                +"http://localhost:8080/auth/generate-new-password?token="
                                +confirmationToken.getConfirmationToken()
                                + "&email=" + email)
                        .build()
        );
    }

    public void confirmUserForgotPassword(String confirmationToken) throws TokenExpiredException {
        Optional<ConfirmationToken> optionalConfirmationToken = confirmationTokenRepository.findByConfirmationToken(confirmationToken);
        if(optionalConfirmationToken.isPresent()) {
            confirmationTokenRepository.delete(optionalConfirmationToken.get());
        }
        else {
            throw new TokenExpiredException();
        }
    }

    public void setNewPasswordForUser(UserPasswordDto userForgot) {
        // Don't need to check ifPresent because we already checked in method sendVerificationEmailForGenerateNewPassword
        User user = userRepository.findByEmail(userForgot.getEmail()).get();
        userRepository.updatePassword(passwordEncoder.encode(userForgot.getPassword()), user.getId());
    }

    public void changeUserPassword(UserDetails currentUser, UserChangePasswordDto userPassword) throws OldPasswordNotCorrectException {
        // Don't need to check ifPresent because we ensure that only if the user has their account they can change their password
        User user = userRepository.findByUsername(currentUser.getUsername()).get();
        if (!user.getPassword().equals(passwordEncoder.encode(userPassword.getOldPassword()))) {
            throw new OldPasswordNotCorrectException();
        }
        userRepository.updatePassword(passwordEncoder.encode(userPassword.getNewPassword()), user.getId());
    }
}
