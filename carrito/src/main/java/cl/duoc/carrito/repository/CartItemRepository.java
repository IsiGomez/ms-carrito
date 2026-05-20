package cl.duoc.carrito.repository;

import cl.duoc.carrito.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

}
