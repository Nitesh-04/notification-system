package com.nk.notification_system.config;

import com.nk.notification_system.model.Subscription;
import com.nk.notification_system.model.User;
import com.nk.notification_system.repository.SubscriptionRepository;
import com.nk.notification_system.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(2)
public class SubscriptionDataSeeder implements CommandLineRunner {

    private final SubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;

    public SubscriptionDataSeeder(SubscriptionRepository subscriptionRepository, UserRepository userRepository) {
        this.subscriptionRepository = subscriptionRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) {
        if(subscriptionRepository.count() > 0) {
            return;
        }
        String[] users = {
                "aarav@gmail.com",
                "ananya@gmail.com",
                "arjun@gmail.com",
                "diya@gmail.com",
                "kabir@gmail.com",
                "meera@gmail.com",
                "rohan@gmail.com",
                "isha@gmail.com",
                "vivaan@gmail.com",
                "nisha@gmail.com"
        };

        for (String email : users) {
            createSubscription(email, "Arsenal");
        }

        for (String email : users) {
            createSubscription(email, "Liverpool");
        }

        for (String email : users) {
            createSubscription(email, "Chelsea");
        }

        for (String email : users) {
            createSubscription(email, "Barcelona");
        }

        System.out.println("40 subs created");
    }

    private void createSubscription(String email, String eventName) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException(
                        "User not found: " + email
                ));

        Subscription subscription = new Subscription();
        subscription.setUser(user);
        subscription.setEventName(eventName);
        subscriptionRepository.save(subscription);
    }
}
