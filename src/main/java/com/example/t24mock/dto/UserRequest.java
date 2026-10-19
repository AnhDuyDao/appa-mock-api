package com.example.t24mock.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.validation.constraints.NotBlank;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserRequest {

  @NotBlank(message = "Missing required field: userId")
  private String userId;

  @NotBlank(message = "Missing required field: userName")
  private String userName;

  private String email;
  private String department;
  private String role;

  @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
  private java.util.List<String> accessRights;
  private Boolean active;

  public UserRequest() {
  }

  public UserRequest(String userId, String userName, String email, String department, String role,
      java.util.List<String> accessRights) {
    this.userId = userId;
    this.userName = userName;
    this.email = email;
    this.department = department;
    this.role = role;
    this.accessRights = accessRights;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getDepartment() {
    return department;
  }

  public String getRole() {
    return role;
  }

  public void setDepartment(String department) {
    this.department = department;
  }

  public void setRole(String role) {
    this.role = role;
  }

  public java.util.List<String> getAccessRights() {
    return accessRights;
  }

  public void setAccessRights(java.util.List<String> accessRights) {
    this.accessRights = accessRights;
  }

  public Boolean getActive() {
    return active;
  }

  public void setActive(Boolean active) {
    this.active = active;
  }
}
