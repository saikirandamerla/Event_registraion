package com.example.eventregistration.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.eventregistration.model.Event;
import com.example.eventregistration.service.EventService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/events")
public class EventController {

    @Autowired
    private EventService eventService;

    @GetMapping
    public String listEvents(Model model) {
        model.addAttribute("events", eventService.getAllEvents());
        return "events-enhanced";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("event", new Event());
        return "event-form";
    }

    @PostMapping
    public String saveEvent(@Valid @ModelAttribute Event event, BindingResult result) {
        if (result.hasErrors()) {
            return "event-form";
        }
        eventService.saveEvent(event);
        return "redirect:/events";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Event event = eventService.getEventById(id).orElseThrow(() -> new IllegalArgumentException("Invalid event Id:" + id));
        model.addAttribute("event", event);
        return "event-form";
    }

    @PostMapping("/update/{id}")
    public String updateEvent(@PathVariable Long id, @Valid @ModelAttribute Event event, BindingResult result) {
        if (result.hasErrors()) {
            return "event-form";
        }
        event.setId(id);
        eventService.saveEvent(event);
        return "redirect:/events";
    }

    @GetMapping("/delete/{id}")
    public String deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
        return "redirect:/events";
    }
}
