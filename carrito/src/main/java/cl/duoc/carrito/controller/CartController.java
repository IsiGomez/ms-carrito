package cl.duoc.carrito.controller;

import cl.duoc.carrito.config.SecurityUtil;
import cl.duoc.carrito.dto.request.CartItemRequestDto;
import cl.duoc.carrito.dto.request.CartRequestDto;
import cl.duoc.carrito.dto.response.CartResponseDto;
import cl.duoc.carrito.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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


    @GetMapping("/user/{userId}/total")
    public ResponseEntity<?> getTotal(
            @Parameter(description = "ID del usuario dueño del carrito", required = true, example = "2")
            @PathVariable Long userId) {

        ResponseEntity<?> forbidden = verificarPropietario(userId);
        if (forbidden != null) return forbidden;

        CartResponseDto cart = cartService.getCart(userId);
        return ResponseEntity.ok(cart.getTotal() != null ? cart.getTotal().doubleValue() : 0.0);
    }


    @Operation(summary = "Obtener todo el carrito de un usuario",
            tags = {"Módulo de Carrito → 1. Consultas de Carrito"})
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getCart(@PathVariable Long userId) {
        ResponseEntity<?> forbidden = verificarPropietario(userId);
        if (forbidden != null) return forbidden;

        return ResponseEntity.ok(cartService.getCart(userId));
    }


    @Operation(summary = "Añadir producto a carrito",
            tags = {"Módulo de Carrito → 2. Acciones de Carrito"})
    @PostMapping("/user/{userId}/item")
    public ResponseEntity<?> addItem(@PathVariable Long userId, @Valid @RequestBody CartRequestDto request) {
        ResponseEntity<?> forbidden = verificarPropietario(userId);

        if (forbidden != null) {
            return forbidden;
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(cartService.addItem(userId, request));
    }


    @Operation(summary = "Actualizar cantidad de un producto que ya existe en el carrito",
            tags = {"Módulo de Carrito → 2. Acciones de Carrito"})
    @PatchMapping("/user/{userId}/item/{productId}")
    public ResponseEntity<?> updateQuantity(@PathVariable Long userId,
                                                          @PathVariable Long productId,
                                                          @Valid @RequestBody CartItemRequestDto request) {
        ResponseEntity<?> forbidden = verificarPropietario(userId);
        if (forbidden != null) {
            return forbidden;
        }

        return ResponseEntity.ok(cartService.updateQuantity(userId, productId, request));
    }


    @Operation(summary = "Eliminar un producto del carrito",
            tags = {"Módulo de Carrito → 2. Acciones de Carrito"})
    @DeleteMapping("/user/{userId}/item/{productId}")
    public ResponseEntity<?> removeItem( @PathVariable Long userId,
                                                       @PathVariable Long productId) {
        ResponseEntity<?> forbidden = verificarPropietario(userId);
        if (forbidden != null) {
            return forbidden;
        }

        return ResponseEntity.ok(cartService.removeItem(userId, productId));
    }


    @Operation(summary = "Dejar el carrito vacio",
            tags = {"Módulo de Carrito → 2. Acciones de Carrito"})
    @DeleteMapping("/user/{userId}/clear")
    public ResponseEntity<?> clearCart(@PathVariable Long userId) {
        ResponseEntity<?> forbidden = verificarPropietario(userId);
        if (forbidden != null) {
            return forbidden;
        }

        cartService.clearCart(userId);

        return ResponseEntity.noContent().build();
    }


    @PostMapping("/user/{userId}/promocion")
    public ResponseEntity<?> aplicarPromocion(
            @PathVariable Long userId,
            @RequestParam String codigo) {

        ResponseEntity<?> forbidden = verificarPropietario(userId);
        if (forbidden != null) return forbidden;

        return ResponseEntity.ok(cartService.aplicarPromocion(userId, codigo));
    }

    @GetMapping("/user/{userId}/canje/simular")
    public ResponseEntity<?> simularCanjePuntos(
            @PathVariable Long userId) {

        ResponseEntity<?> forbidden = verificarPropietario(userId);
        if (forbidden != null) return forbidden;

        return ResponseEntity.ok(cartService.simularCanjePuntos(userId));
    }


    @PostMapping("/user/{userId}/canje/confirmar")
    public ResponseEntity<?> confirmarCanjePuntos(
            @PathVariable Long userId) {

        ResponseEntity<?> forbidden = verificarPropietario(userId);
        if (forbidden != null) return forbidden;

        return ResponseEntity.ok(cartService.confirmarCanjePuntos(userId));
    }


    private ResponseEntity<?> verificarPropietario(Long userIdDeLaUrl) {
        Long userIdDelToken = SecurityUtil.currentUserId();

        if (userIdDelToken == null || !userIdDelToken.equals(userIdDeLaUrl)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Acceso denegado: no puedes operar sobre el carrito de otro usuario.");
        }
        return null;
    }
}
