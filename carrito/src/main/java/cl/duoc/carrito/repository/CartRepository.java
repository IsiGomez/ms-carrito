package cl.duoc.carrito.repository;

import cl.duoc.carrito.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {

}
