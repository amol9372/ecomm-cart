package org.ecomm.ecommcart.rest.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.Optional;
import java.util.function.Function;
import lombok.extern.slf4j.Slf4j;
import org.ecomm.ecommcart.persistance.entity.cart.CartStatus;
import org.ecomm.ecommcart.persistance.entity.cart.ECart;
import org.ecomm.ecommcart.persistance.entity.cart.ECartItem;
import org.ecomm.ecommcart.persistance.repository.CartRepository;
import org.ecomm.ecommcart.rest.model.UserResponse;
import org.ecomm.ecommcart.rest.request.AddToCartRequest;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CartServiceImpl implements CartService {

  @Autowired CartRepository cartRepository;

  @Override
  @Transactional
  public void addProductToCart(AddToCartRequest request) {
    // check if cart already exists
    UserResponse user = getLoggedInUser();

    var activeCart =
        cartRepository
            .findByUserIdAndStatus(user.getId(), CartStatus.ACTIVE)
            .orElse(createCart.apply(user.getId()));

    var cartItem =
            ECartItem.builder()
                    .cart(activeCart)
                    .productId(request.getProductId())
                    .quantity(request.getQuantity())
                    .variantId(request.getVariantId())
                    .build();

    addItemToCart(activeCart, cartItem);

    cartRepository.save(activeCart);
  }

  private static UserResponse getLoggedInUser() {
    ObjectMapper objectMapper = new ObjectMapper();

    UserResponse user;
    try {
      user = objectMapper.readValue(MDC.get("user"), UserResponse.class);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
    return user;
  }

  private static void addItemToCart(ECart activeCart, ECartItem cartItem) {
    Optional.ofNullable(activeCart.getCartItems())
        .ifPresentOrElse(
            eCartItems -> activeCart.getCartItems().add(cartItem),
            () -> {
              var cartItems = new ArrayList<ECartItem>();
              cartItems.add(cartItem);
              activeCart.setCartItems(cartItems);
            });
  }

  Function<Integer, ECart> createCart =
      (userId) ->
          ECart.builder().userId(userId).status(CartStatus.ACTIVE).build();
}
