package org.ecomm.ecommcart.rest.feign.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductInventory {
    @JsonProperty("quantityAvailable")
    int quantity;

    int variantId;
}
