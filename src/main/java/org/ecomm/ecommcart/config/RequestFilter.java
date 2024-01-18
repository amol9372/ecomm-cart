package org.ecomm.ecommcart.config;

import static org.springframework.util.ObjectUtils.isEmpty;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.ecomm.ecommcart.exception.ErrorCodes;
import org.ecomm.ecommcart.exception.ErrorResponse;
import org.ecomm.ecommcart.exception.UnauthorizedUserException;
import org.ecomm.ecommcart.rest.feign.UserServiceClient;
import org.ecomm.ecommcart.rest.model.UserResponse;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Order(2)
@Component
@Slf4j
public class RequestFilter extends OncePerRequestFilter {

  private final List<String> permittedUrls = List.of("/user");

  @Autowired UserServiceClient userServiceClient;

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    log.info("Request ::: {}", request);
    // String requestMethod = request.getMethod();
    String userEmail = request.getHeader("x-auth0-user-email");
    if (isEmpty(userEmail)) {
      log.error("User email header missing [x-auth0-user-email]");

      var errorResponse =
          ErrorResponse.builder()
              .code(ErrorCodes.MISSING_EMAIL_HEADER)
              .message(Constants.UNAUTHORIZED_USER)
              .build();
      throw new UnauthorizedUserException(HttpStatus.UNAUTHORIZED, errorResponse);
    }

    ResponseEntity<UserResponse> basicUserInfo =
        userServiceClient.getUserByEmail(userEmail, "ecomm-cart");

    if (basicUserInfo.getStatusCode().is2xxSuccessful()) {
      ObjectMapper objectMapper = new ObjectMapper();

      //  Convert Java object to JSON string
      String userString = objectMapper.writeValueAsString(basicUserInfo.getBody());
      MDC.put("user", userString);
    } else {
      var errorResponse =
          ErrorResponse.builder()
              .code(ErrorCodes.USER_DOES_NOT_EXIST)
              .message(Constants.UNAUTHORIZED_USER)
              .build();
      throw new UnauthorizedUserException(HttpStatus.UNAUTHORIZED, errorResponse);
    }

    filterChain.doFilter(request, response);
  }
}
