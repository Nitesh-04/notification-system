package com.nk.notification_system.service;

import com.nk.notification_system.dto.UserResponse;
import com.nk.notification_system.model.User;
import com.nk.notification_system.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(user -> new UserResponse(
                        user.getUserId(),
                        user.getName(),
                        user.getEmail()
                ))
                .toList();
    }

}
