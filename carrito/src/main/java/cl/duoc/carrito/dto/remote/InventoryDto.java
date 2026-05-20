package cl.duoc.carrito.dto.remote;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter             @Setter
@AllArgsConstructor @NoArgsConstructor
public class InventoryDto {

    private Long id;
    private Long productId;
    private Integer quantity;

}
