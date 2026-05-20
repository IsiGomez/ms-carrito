package cl.duoc.carrito.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter             @Setter
@AllArgsConstructor @NoArgsConstructor
public class CartResponseDto {

    private Long id;
    private Long userId;

    private List<CartItemResponseDto> items;
    private Integer total;

}
