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

    @Operation(summary = "Obtener todo el carrito de un usuario",
            tags = {"Módulo de Carrito → 1. Consultas de Carrito"})
    @GetMapping("/user/{userId}")
    public ResponseEntity<CartResponseDto> getCart(@PathVariable Long userId) {
        return ResponseEntity.ok(cartService.getCart(userId));
    }


    @Operation(summary = "Añadir producto a carrito",
            tags = {"Módulo de Carrito → 2. Acciones de Carrito"})
    @PostMapping("/user/{userId}/item")
    public ResponseEntity<CartResponseDto> addItem(@PathVariable Long userId, @Valid @RequestBody CartRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cartService.addItem(userId, request));
    }


    @Operation(summary = "Actualizar cantidad de un producto que ya existe en el carrito",
            tags = {"Módulo de Carrito → 2. Acciones de Carrito"})
    @PatchMapping("/user/{userId}/item/{productId}")
    public ResponseEntity<CartResponseDto> updateQuantity(@PathVariable Long userId,
                                                          @PathVariable Long productId,
                                                          @Valid @RequestBody CartItemRequestDto request) {
        return ResponseEntity.ok(cartService.updateQuantity(userId, productId, request));
    }


    @Operation(summary = "Eliminar un producto del carrito",
            tags = {"Módulo de Carrito → 2. Acciones de Carrito"})
    @DeleteMapping("/user/{userId}/item/{productId}")
    public ResponseEntity<CartResponseDto> removeItem( @PathVariable Long userId,
                                                       @PathVariable Long productId) {
        return ResponseEntity.ok(cartService.removeItem(userId, productId));
    }


    @Operation(summary = "Dejar el carrito vacio",
            tags = {"Módulo de Carrito → 2. Acciones de Carrito"})
    @DeleteMapping("/user/{userId}/clear")
    public ResponseEntity<Void> clearCart(@PathVariable Long userId) {
        cartService.clearCart(userId);

        return ResponseEntity.noContent().build();
    }

}
