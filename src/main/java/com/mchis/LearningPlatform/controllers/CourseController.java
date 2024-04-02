package com.mchis.LearningPlatform.controllers;

import com.mchis.LearningPlatform.entities.Course;
import com.mchis.LearningPlatform.services.CourseService;
import com.mchis.LearningPlatform.services.UserService;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@AllArgsConstructor
@Data
@RequestMapping("/courses")
public class CourseController{
    private final CourseService courseService;
    private final UserService userService;

    @GetMapping("/create")
    public String createNewCourse(
            Model model
    ) {
        model.addAttribute("course", new Course());
        return "create-new-course";
    }

    @PostMapping("/create")
    public String createNewCourse(
            @ModelAttribute Course course,
            @AuthenticationPrincipal UserDetails currentUser
    ) {
        courseService.createNewCourse(course, currentUser);
        return "redirect:/courses/own";
    }

    @GetMapping(value = {"", "/"})
    public String showAllCourses(
            Model model,
            @AuthenticationPrincipal UserDetails currentUser
    ) {
        model.addAttribute(
                "courses",
                courseService.getAllNotTeachingAndNotStudyingCourses(currentUser));
        return "all-courses";
    }

    @GetMapping("/{id}")
    public String showCourseByIdInAllMode(
            @PathVariable("id") Integer id,
            Model model
    ) {
        Course course = courseService.getCourseById(id);
        model.addAttribute("course", course);
        model.addAttribute("teach", "");           // if teach then own/
        model.addAttribute("register", "");        // if register then deregister/
        model.addAttribute("none", "Register");    // if none then register/
        return "single-course";
    }

    @GetMapping("/own")
    public String showAllCoursesTaughtByCurrentUser(
            Model model,
            @AuthenticationPrincipal UserDetails currentUser
    ) {
        model.addAttribute(
                "courses",
                userService.getAllTeachingCourses(currentUser));
        return "teaching-courses";
    }

    @GetMapping("/own/{id}")
    public String showCourseTaughtByCurrentUser(
            @PathVariable("id") Integer id,
            Model model
    ) {
        Course course = courseService.getCourseById(id);
        model.addAttribute("course", course);
        model.addAttribute("teach", "teach");           // if teach then own/
        model.addAttribute("register", "");             // if register then deregister/
        model.addAttribute("none", "");                 // if none then register/
        return "single-course";
    }

    @PostMapping("/own/{id}")
    public String editCourseTaughtByCurrentUser(
            @PathVariable("id") Integer id,
            @ModelAttribute Course course
            ) {
        courseService.edit(course, id);
        return "redirect:/courses/own/" + id;
    }


    @GetMapping("/registered")
    public String showAllCoursesOfCurrentUser(
            Model model,
            @AuthenticationPrincipal UserDetails currentUser
    ) {
        model.addAttribute(
                "courses",
                userService.getAllStudyingCourses(currentUser));
        return "registered-courses";
    }

    @GetMapping("/registered/{id}")
    public String showCourseByIdInRegisterMode(
            @PathVariable("id") Integer id,
            Model model
    ) {
        Course course = courseService.getCourseById(id);
        model.addAttribute("course", course);
        model.addAttribute("teach", "");                // if teach then own/
        model.addAttribute("register", "Deregister");   // if register then deregister/
        model.addAttribute("none", "");                 // if none then register/
        return "single-course";
    }

    @PostMapping("/register/{id}")
    public String register(
            @AuthenticationPrincipal UserDetails currentUser,
            @PathVariable("id") Integer id
    ){
        courseService.register(id, currentUser);
        return "redirect:/courses/registered";
    }

    @PostMapping("/deregister/{id}")
    public String deregister(
            @AuthenticationPrincipal UserDetails currentUser,
            @PathVariable("id") Integer id
    ) {
        courseService.deregister(id, currentUser);
        return "redirect:/courses/registered";
    }

}
