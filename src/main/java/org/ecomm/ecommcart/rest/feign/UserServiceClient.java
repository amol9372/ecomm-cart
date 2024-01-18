package org.ecomm.ecommcart.rest.feign;

import org.ecomm.ecommcart.rest.model.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "user-service", url = "http://localhost:8080/api/user")
public interface UserServiceClient {

  @GetMapping(value = "{email}")
  ResponseEntity<UserResponse> getUserByEmail(@PathVariable("email") String email, @RequestHeader("service") String service);


}
