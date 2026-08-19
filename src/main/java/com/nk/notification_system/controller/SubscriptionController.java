package com.nk.notification_system.controller;

import com.nk.notification_system.dto.CreateSubRequest;
import com.nk.notification_system.dto.SubscriptionResponse;
import com.nk.notification_system.service.SubscriptionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/subscriptions")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    public SubscriptionController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<SubscriptionResponse>> findAllSubscriptions() {
        List<SubscriptionResponse> subscriptions = subscriptionService.findAllSubscriptions();
        return new ResponseEntity<>(subscriptions, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<SubscriptionResponse> createSubscription(@Valid @RequestBody CreateSubRequest subRequest) {
        var subscription = subscriptionService.createSubscription(subRequest);
        var response = new SubscriptionResponse(
                subscription.getSubId(),
                subscription.getUser().getUserId(),
                subscription.getEventName()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}
