 package com.example.eventregistration.controller;

import com.example.eventregistration.model.Event;
import com.example.eventregistration.model.Registration;
import com.example.eventregistration.model.User;
import com.example.eventregistration.service.EventService;
import com.example.eventregistration.service.RegistrationService;
import com.example.eventregistration.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/registrations")
public class RegistrationController {

    @Autowired
    private RegistrationService registrationService;

    @Autowired
    private EventService eventService;

    @Autowired
    private UserService userService;

    @GetMapping
    public String listRegistrations(Authentication authentication, Model model) {
        User user = userService.findByUsername(authentication.getName());
        model.addAttribute("registrations", registrationService.getRegistrationsByUser(user));
        return "registrations";
    }

    @GetMapping("/new")
    public String showCreateForm(@RequestParam Long eventId, Authentication authentication, Model model) {
        Event event = eventService.getEventById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid event Id:" + eventId));
        
        Registration registration = new Registration();
        registration.setEvent(event);
        
        model.addAttribute("registration", registration);
        model.addAttribute("event", event);
        return "registration-form";
    }

    @PostMapping
    public String saveRegistration(@Valid @ModelAttribute Registration registration, 
                                   BindingResult result, 
                                   Authentication authentication,
                                   Model model) {
        if (result.hasErrors()) {
            model.addAttribute("event", registration.getEvent());
            return "registration-form";
        }
        
        User user = userService.findByUsername(authentication.getName());
        registration.setUser(user);
        registrationService.saveRegistration(registration);
        return "redirect:/registrations";
    }

    @GetMapping("/delete/{id}")
    public String deleteRegistration(@PathVariable Long id, Authentication authentication) {
        User user = userService.findByUsername(authentication.getName());
        Registration registration = registrationService.getRegistrationById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid registration Id:" + id));
        
        // Only allow users to delete their own registrations
        if (registration.getUser().getId().equals(user.getId())) {
            registrationService.deleteRegistration(id);
        }
        return "redirect:/registrations";
    }
}
