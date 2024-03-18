package com.mchis.LearningPlatform.user;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

// @RestController
@Controller
public class UserController {
    //@GetMapping("/")
    public String controller(Model model) {
        return "login";
    }
}
