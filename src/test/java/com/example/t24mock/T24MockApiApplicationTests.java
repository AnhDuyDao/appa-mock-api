package com.example.t24mock;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.t24mock.dto.UserRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
class T24MockApiApplicationTests {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  void healthCheckShouldReturnUp() throws Exception {
    mockMvc.perform(get("/health"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value("UP"));
  }

  @Test
  void createUserShouldReturnCreated() throws Exception {
    UserRequest request = new UserRequest("U001", "Nguyen Van A", "a@example.com", "IT", "ADMIN", null);

    mockMvc.perform(post("/api/users")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.status").value("SUCCESS"))
        .andExpect(jsonPath("$.userId").value("U001"));
  }

  @Test
  void createUserShouldRejectMissingUserId() throws Exception {
    String payload = "{\"userName\":\"Nguyen Van A\",\"email\":\"a@example.com\",\"department\":\"IT\",\"role\":\"ADMIN\"}";

    mockMvc.perform(post("/api/users")
        .contentType(MediaType.APPLICATION_JSON)
        .content(payload))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.error").value("Missing required field: userId"));
  }

  @Test
  void createUserShouldRejectDuplicateUserId() throws Exception {
    UserRequest request = new UserRequest("U002", "User Duplicate", "dup@example.com", "IT", "USER", null);

    mockMvc.perform(post("/api/users")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated());

    mockMvc.perform(post("/api/users")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isConflict())
        .andExpect(jsonPath("$.error").value("User already exists"));
  }

  @Test
  void updateUserShouldReturnUpdatedUser() throws Exception {
    UserRequest createRequest = new UserRequest("U003", "Old Name", "old@example.com", "IT", "USER", null);
    UserRequest updateRequest = new UserRequest("U003", "New Name", "new@example.com", "HR", "ADMIN", null);

    mockMvc.perform(post("/api/users")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(createRequest)))
        .andExpect(status().isCreated());

    mockMvc.perform(put("/api/users/U003")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(updateRequest)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.userId").value("U003"))
        .andExpect(jsonPath("$.userName").value("New Name"))
        .andExpect(jsonPath("$.department").value("HR"));
  }

  @Test
  void deleteUserShouldRemoveUser() throws Exception {
    UserRequest request = new UserRequest("U004", "User To Delete", "delete@example.com", "IT", "USER", null);

    mockMvc.perform(post("/api/users")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated());

    mockMvc.perform(delete("/api/users/U004"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value("SUCCESS"));

    mockMvc.perform(get("/api/users/U004"))
        .andExpect(status().isNotFound());
  }
}
