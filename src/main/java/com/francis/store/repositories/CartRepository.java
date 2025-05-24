package com.francis.store.repositories;

import com.francis.store.entities.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CartRepository extends JpaRepository<Cart, UUID> {
   // @EntityGraph(attributePaths =  "items.product")
   // @Query("SELECT c FROM Cart c WHERE c.id = :cartId")
   // Optional<Cart> getCartWithItems(@Param("cartId") UUID cartId);

   // @EntityGraph(attributePaths = "items.product")
  //  @Query("SELECT c FROM Cart c WHERE c.id = :cartId")
  //  Optional<Cart> getCartWithItems(@Param("cartId") UUID cartId);
}