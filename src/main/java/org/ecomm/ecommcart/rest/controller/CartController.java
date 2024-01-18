package org.ecomm.ecommcart.rest.controller;

import org.ecomm.ecommcart.rest.request.AddToCartRequest;
import org.ecomm.ecommcart.rest.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("cart")
public class CartController {

  @Autowired CartService cartService;

  @PostMapping
  public void addToCart(@RequestBody AddToCartRequest request) {
    cartService.addProductToCart(request);
  }
}
