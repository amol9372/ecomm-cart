package org.ecomm.ecommcart.rest.model;

import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    int id;
    String firstName;
    String lastName;
    String email;
    String role;
}
