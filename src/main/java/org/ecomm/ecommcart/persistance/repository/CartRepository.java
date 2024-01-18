package org.ecomm.ecommcart.persistance.repository;

import org.ecomm.ecommcart.persistance.entity.cart.CartStatus;
import org.ecomm.ecommcart.persistance.entity.cart.ECart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<ECart, Integer> {

    Optional<ECart> findByUserIdAndStatus(int userId, CartStatus cartStatus);
}
