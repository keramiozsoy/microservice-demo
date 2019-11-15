package com.microservice.demo.client3.controllers;

import javax.annotation.security.RolesAllowed;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WelcomeController {

  @GetMapping("/public")
  public String welcomePublic() {
    return "welcome public/guest user";
  }

  @RolesAllowed({"ROLE_ADMIN"})
  @GetMapping("/admin")
  public String welcomeAdmin() {
    return "welcome admin";
  }

  @RolesAllowed({"ROLE_USER"})
  @GetMapping("/user")
  public String welcomeUser() {
    return "welcome user";
  }
}
