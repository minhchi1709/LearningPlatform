package com.mchis.LearningPlatform.controllers;

import org.springframework.web.bind.annotation.GetMapping;

public class ErrorController {
    @GetMapping("/error")
    public String error() {
        return "error";
    }
}
