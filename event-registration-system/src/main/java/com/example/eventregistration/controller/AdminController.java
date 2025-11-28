package com.example.eventregistration.controller;

import com.example.eventregistration.model.Event;
import com.example.eventregistration.model.Registration;
import com.example.eventregistration.model.User;
import com.example.eventregistration.service.EventService;
import com.example.eventregistration.service.RegistrationService;
import com.example.eventregistration.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private EventService eventService;

    @Autowired
    private RegistrationService registrationService;

    @Autowired
    private UserService userService;

    @GetMapping
    public String adminDashboard(Model model) {
        model.addAttribute("totalEvents", eventService.getAllEvents().size());
        model.addAttribute("totalRegistrations", registrationService.getAllRegistrations().size());
        model.addAttribute("totalUsers", userService.getAllUsers().size());
        return "admin/dashboard";
    }

    // Event Management
    @GetMapping("/events")
    public String manageEvents(Model model) {
        model.addAttribute("events", eventService.getAllEvents());
        return "admin/events";
    }

    @GetMapping("/events/new")
    public String showCreateEventForm(Model model) {
        model.addAttribute("event", new Event());
        return "admin/event-form";
    }

    @PostMapping("/events")
    public String createEvent(@Valid @ModelAttribute("event") Event event, 
                            BindingResult result, 
                            Model model) {
        if (result.hasErrors()) {
            return "admin/event-form";
        }
        eventService.saveEvent(event);
        return "redirect:/admin/events";
    }

    @GetMapping("/events/edit/{id}")
    public String showEditEventForm(@PathVariable Long id, Model model) {
        Event event = eventService.getEventById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid event Id:" + id));
        model.addAttribute("event", event);
        return "admin/event-form";
    }

    @PostMapping("/events/update/{id}")
    public String updateEvent(@PathVariable Long id, 
                            @Valid @ModelAttribute("event") Event event, 
                            BindingResult result, 
                            Model model) {
        if (result.hasErrors()) {
            return "admin/event-form";
        }
        event.setId(id);
        eventService.saveEvent(event);
        return "redirect:/admin/events";
    }

    @GetMapping("/events/delete/{id}")
    public String deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
        return "redirect:/admin/events";
    }

    // Registration Management
    @GetMapping("/registrations")
    public String manageRegistrations(Model model) {
        model.addAttribute("registrations", registrationService.getAllRegistrations());
        return "admin/registrations";
    }

    @GetMapping("/registrations/event/{eventId}")
    public String viewEventRegistrations(@PathVariable Long eventId, Model model) {
        Event event = eventService.getEventById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid event Id:" + eventId));
        List<Registration> registrations = registrationService.getRegistrationsByEvent(eventId);
        model.addAttribute("event", event);
        model.addAttribute("registrations", registrations);
        return "admin/event-registrations";
    }

    @GetMapping("/registrations/delete/{id}")
    public String deleteRegistration(@PathVariable Long id) {
        registrationService.deleteRegistration(id);
        return "redirect:/admin/registrations";
    }

    // User Management
    @GetMapping("/users")
    public String manageUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "admin/users";
    }

    @GetMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "redirect:/admin/users";
    }
}
