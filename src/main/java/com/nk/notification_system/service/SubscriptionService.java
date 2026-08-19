package com.nk.notification_system.service;

import com.nk.notification_system.dto.CreateSubRequest;
import com.nk.notification_system.dto.SubscriptionResponse;
import com.nk.notification_system.model.Subscription;
import com.nk.notification_system.model.User;
import com.nk.notification_system.repository.SubscriptionRepository;
import com.nk.notification_system.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;

    public SubscriptionService(SubscriptionRepository subscriptionRepository, UserRepository userRepository) {
        this.subscriptionRepository = subscriptionRepository;
        this.userRepository = userRepository;
    }

    public List<SubscriptionResponse> findAllSubscriptions() {
        return subscriptionRepository.findAll()
                .stream()
                .map(subscription -> new SubscriptionResponse(
                        subscription.getSubId(),
                        subscription.getUser().getUserId(),
                        subscription.getEventName()
                ))
                .toList();
    }

    @Transactional
    public Subscription createSubscription(CreateSubRequest request) {
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "User not found"
                ));

        if (subscriptionRepository.existsByUserUserIdAndEventName(
                request.userId(), request.eventName())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Subscription already exists"
            );
        }
        Subscription subscription = new Subscription();
        subscription.setUser(user);
        subscription.setEventName(request.eventName());
        return subscriptionRepository.save(subscription);
    }
}
