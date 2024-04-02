package com.mchis.LearningPlatform.controllers;

import com.mchis.LearningPlatform.dto.UserDto;
import com.mchis.LearningPlatform.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/user")
@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    @GetMapping({"", "/"})
    public String profile(
            Model model,
            @AuthenticationPrincipal UserDetails currentUser
    ) {
        model.addAttribute("user", userService.convertUserToUserDto(userService.loadUserByCurrentUser(currentUser)));
        return "profile";
    }

    @PostMapping("/edit")
    public String editProfile(
            @ModelAttribute UserDto newUser,
            @AuthenticationPrincipal UserDetails user
            ) {
        userService.editUser(user, newUser);
        return "redirect:/user";
    }

}
