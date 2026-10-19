package com.example.t24mock.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import com.example.t24mock.dto.UserRequest;
import com.example.t24mock.dto.UserResponse;

@Service
public class UserService {

  private final Map<String, UserResponse> userStore = new ConcurrentHashMap<>();

  public UserService() {
    UserResponse defaultUser = new UserResponse(
        "U100",
        "Nguyen Van A",
        "a@example.com",
        "IT",
        "USER",
        List.of("HR:Read", "Finance:Write"));

    defaultUser.setActive(true);
    userStore.put(defaultUser.getUserId(), defaultUser);
  }

  public UserResponse createUser(UserRequest request) {
    if (userStore.containsKey(request.getUserId())) {
      throw new IllegalArgumentException("User already exists");
    }

    UserResponse userResponse = new UserResponse(
        request.getUserId(),
        request.getUserName(),
        request.getEmail(),
        request.getDepartment(),
        request.getRole(),
        request.getAccessRights());
    userResponse.setActive(true);

    userStore.put(request.getUserId(), userResponse);
    return userResponse;
  }

  public Optional<UserResponse> getUserById(String userId) {
    return Optional.ofNullable(userStore.get(userId));
  }

  public Optional<UserResponse> updateUser(String userId, UserRequest request) {
    if (!userStore.containsKey(userId)) {
      return Optional.empty();
    }

    UserResponse existingUser = userStore.get(userId);
    UserResponse updatedUser = new UserResponse(
        userId,
        request.getUserName(),
        request.getEmail(),
        request.getDepartment(),
        request.getRole(),
        request.getAccessRights());
    updatedUser.setActive(request.getActive() != null
        ? request.getActive()
        : existingUser.isActive());
    userStore.put(userId, updatedUser);
    return Optional.of(updatedUser);
  }

  public boolean deleteUser(String userId) {
    return userStore.remove(userId) != null;
  }

  public List<UserResponse> getAllUsers() {
    return new ArrayList<>(userStore.values());
  }
}
