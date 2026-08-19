package com.nk.notification_system.config;

import com.nk.notification_system.model.User;
import com.nk.notification_system.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1)
public class UserDataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;

    public UserDataSeeder(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) {
        if(userRepository.count() > 0) {
            return;
        }

        String[] names = {
                "Aarav", "Ananya", "Arjun", "Diya", "Kabir",
                "Meera", "Rohan", "Isha", "Vivaan", "Nisha",
                "Aditya", "Priya", "Rahul", "Sneha", "Karan",
                "Pooja", "Vikram", "Neha", "Sanjay", "Tanya"
        };

        for(String name : names) {
            User user = new User();
            user.setName(name);
            user.setEmail(name.toLowerCase() + "@gmail.com");
            userRepository.save(user);
        }

        System.out.println("20 Users created");
    }
}
