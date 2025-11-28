package com.example.eventregistration.controller;

import com.example.eventregistration.model.User;
import com.example.eventregistration.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    @Autowired
    private UserService userService;

    @GetMapping
    public String showProfile(Authentication authentication, Model model) {
        User user = userService.findByUsername(authentication.getName());
        model.addAttribute("user", user);
        return "profile";
    }

    @PostMapping("/update")
    public String updateProfile(@Valid @ModelAttribute User user,
                               BindingResult result,
                               Authentication authentication,
                               Model model) {
        if (result.hasErrors()) {
            return "profile";
        }

        try {
            User currentUser = userService.findByUsername(authentication.getName());
            user.setId(currentUser.getId());
            userService.updateProfile(user);
            model.addAttribute("success", "Profile updated successfully");
            model.addAttribute("user", userService.findById(user.getId()));
            return "profile";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "profile";
        }
    }

    @PostMapping("/change-password")
    public String changePassword(@RequestParam String newPassword,
                                @RequestParam String confirmPassword,
                                Authentication authentication,
                                Model model) {
        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("error", "Passwords do not match");
            User user = userService.findByUsername(authentication.getName());
            model.addAttribute("user", user);
            return "profile";
        }

        try {
            User user = userService.findByUsername(authentication.getName());
            userService.changePassword(user.getId(), newPassword);
            model.addAttribute("success", "Password changed successfully");
            model.addAttribute("user", user);
            return "profile";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            User user = userService.findByUsername(authentication.getName());
            model.addAttribute("user", user);
            return "profile";
        }
    }
}
