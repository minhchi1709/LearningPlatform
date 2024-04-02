package com.mchis.LearningPlatform.controllers;

import com.mchis.LearningPlatform.dto.UserChangePasswordDto;
import com.mchis.LearningPlatform.dto.UserForgotPasswordDto;
import com.mchis.LearningPlatform.dto.UserPasswordDto;
import com.mchis.LearningPlatform.exceptions.OldPasswordNotCorrectException;
import com.mchis.LearningPlatform.exceptions.TokenExpiredException;
import com.mchis.LearningPlatform.exceptions.UserAlreadyExistException;
import com.mchis.LearningPlatform.services.AuthenticationService;
import com.mchis.LearningPlatform.entities.User;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Data
@RequiredArgsConstructor
@RequestMapping("auth")
@Controller
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("loginUser", new User());
        return "login";
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("registerUser", new User());
        return "register";
    }

    @PostMapping("/register")
    public String register(
            @ModelAttribute User registerUser,
            Model model
    ) {
        try {
            authenticationService.registerUser(registerUser);
            model.addAttribute("email", registerUser.getEmail());
            return "successful-registration";
        } catch (UserAlreadyExistException exp) {
            model.addAttribute("message", exp.getMessage());
            return "successful-verification";
        }
    }

    @RequestMapping(value="/confirm-account", method= {RequestMethod.GET, RequestMethod.POST})
    public String confirmUserAccount(Model model, @RequestParam("token") String confirmationToken)
    {
        try {
            authenticationService.confirmUserAccount(confirmationToken);
            return "successful-verification";
        } catch(TokenExpiredException exc) {
            model.addAttribute("message", exc.getMessage());
            return "error";
        }

    }

    @GetMapping("/forgot-password")
    public String forgotPassword(
            Model model
    ) {
        model.addAttribute("user", new UserForgotPasswordDto());
        return "forgot-password-form";
    }

    @PostMapping("/forgot-password")
    public String forgotPassword(
            @ModelAttribute UserForgotPasswordDto user,
            Model model
    ) {
        try {
            model.addAttribute("email", user.getEmail());
            authenticationService.sendVerificationEmailForGenerateNewPassword(user.getEmail());
            return "successful-registration";
        } catch(UsernameNotFoundException exp) {
            model.addAttribute("message", exp.getMessage());
            return "error";
        }
    }

    @GetMapping("/generate-new-password")
    public String generateNewPassword(
            @RequestParam("token") String confirmationToken,
            @RequestParam("email") String email,
            Model model
    ) {
        try {
            authenticationService.confirmUserForgotPassword(confirmationToken);
            model.addAttribute("user", UserPasswordDto.builder()
                    .email(email).build());
            return "generate-new-password";
        } catch(TokenExpiredException exp) {
            model.addAttribute("message", exp.getMessage());
            return "error";
        }
    }

    @PostMapping("/generate-new-password")
    public String generateNewPassword(
            @ModelAttribute UserPasswordDto user
    ) {
        authenticationService.setNewPasswordForUser(user);
        return "successfully-set-new-password";
    }

    @GetMapping("/change-password")
    public String changePassword(
            Model model
    ) {
        model.addAttribute("user", UserChangePasswordDto.builder().build());
        return "change-password";
    }

    @PostMapping("/change-password")
    public String changePassword(
            @ModelAttribute UserChangePasswordDto userPassword,
            @AuthenticationPrincipal UserDetails currentUser,
            Model model
            ) {
        try {
            authenticationService.changeUserPassword(currentUser, userPassword);
            return "successfully-change-password";
        } catch (OldPasswordNotCorrectException exp) {
            model.addAttribute("message", exp.getMessage());
            return "error";
        }
    }
}
