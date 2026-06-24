package cl.duoc.carrito.dto.remote;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter             @Setter
@AllArgsConstructor @NoArgsConstructor
public class CanjeConfirmacionDto {

    private Integer puntosCanjeados;
    private Integer montoDescuento;
    private Integer puntosRestantes;

}
