package cl.duoc.carrito.controller;

import cl.duoc.carrito.dto.request.CartItemRequestDto;
import cl.duoc.carrito.dto.request.CartRequestDto;
import cl.duoc.carrito.dto.response.CartResponseDto;
import cl.duoc.carrito.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/carts")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;


    @GetMapping("/user/{userId}")
    public ResponseEntity<CartResponseDto> getCart(@PathVariable Long userId) {
        return ResponseEntity.ok(cartService.getCart(userId));
    }


    @PostMapping("/user/{userId}/item")
    public ResponseEntity<CartResponseDto> addItem(@PathVariable Long userId, @Valid @RequestBody CartRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cartService.addItem(userId, request));
    }


    @PatchMapping("/user/{userId}/item/{productId}")
    public ResponseEntity<CartResponseDto> updateQuantity(@PathVariable Long userId,
                                                          @PathVariable Long productId,
                                                          @Valid @RequestBody CartItemRequestDto request) {
        return ResponseEntity.ok(cartService.updateQuantity(userId, productId, request));
    }


    @DeleteMapping("/user/{userId}/item/{productId}")
    public ResponseEntity<CartResponseDto> removeItem( @PathVariable Long userId,
                                                       @PathVariable Long productId) {
        return ResponseEntity.ok(cartService.removeItem(userId, productId));
    }


    @DeleteMapping("/user/{userId}/clear")
    public ResponseEntity<Void> clearCart(@PathVariable Long userId) {
        cartService.clearCart(userId);
        return ResponseEntity.noContent().build();
    }

}
