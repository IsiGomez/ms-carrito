package cl.duoc.carrito.service;

import cl.duoc.carrito.dto.request.CartItemRequestDto;
import cl.duoc.carrito.dto.request.CartRequestDto;
import cl.duoc.carrito.dto.response.CartResponseDto;

public interface CartService {

    CartResponseDto getCart(Long userId);

    CartResponseDto addItem(Long userId, CartRequestDto request);

    CartResponseDto updateQuantity(Long userId, Long productId, CartItemRequestDto request);

    CartResponseDto removeItem(Long userId, Long productId);

    void clearCart(Long userId);

}
