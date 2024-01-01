package org.ecomm.ecommuser.rest.controller;

import java.net.URI;
import java.util.Map;

import org.ecomm.ecommuser.rest.model.User;
import org.ecomm.ecommuser.rest.request.AddUserRequest;
import org.ecomm.ecommuser.rest.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("user")
public class UserController {

  @Autowired UserService userService;

  @PostMapping
  public ResponseEntity<Object> createAppUser(@RequestBody AddUserRequest request) {
    var user = userService.createAppUser(request);

    return ResponseEntity.created(URI.create("/user")).body(Map.of("id", user.getId()));
  }
}