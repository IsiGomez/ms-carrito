package cl.duoc.carrito.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter             @Setter
@AllArgsConstructor @NoArgsConstructor
public class CartItemResponseDto {

    private Long id;
    private Long productId;
    private String productName;
    private Integer quantity;
    private Integer subtotal;

}
