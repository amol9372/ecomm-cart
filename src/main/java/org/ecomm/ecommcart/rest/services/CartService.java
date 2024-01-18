package org.ecomm.ecommcart.rest.services;

import org.ecomm.ecommcart.rest.request.AddToCartRequest;
import org.springframework.stereotype.Service;

@Service
public interface CartService {

    void addProductToCart(AddToCartRequest request);

}
