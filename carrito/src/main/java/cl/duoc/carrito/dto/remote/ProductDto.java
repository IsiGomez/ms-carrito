package cl.duoc.carrito.dto.remote;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter             @Setter
@AllArgsConstructor @NoArgsConstructor
public class ProductDto {

    private Long id;
    private String name;
    private String description;
    private Integer price;
    private CategoryDto category;

}
