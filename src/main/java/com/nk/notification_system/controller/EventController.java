package com.nk.notification_system.controller;

import com.nk.notification_system.dto.CreateEventDTO;
import com.nk.notification_system.dto.EventResponse;
import com.nk.notification_system.model.Event;
import com.nk.notification_system.service.EventService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/events")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<EventResponse>> getAllEvents() {
        List<EventResponse> events = eventService.findAllEvents();

        return new ResponseEntity<>(events, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<EventResponse> createEvent(@Valid @RequestBody CreateEventDTO createEventDTO) {
        Event event = eventService.createEvent(createEventDTO);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(eventService.toResponse(event));
    }

}
