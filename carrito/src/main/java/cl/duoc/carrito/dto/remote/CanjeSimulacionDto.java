package cl.duoc.carrito.dto.remote;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter             @Setter
@AllArgsConstructor @NoArgsConstructor
public class CanjeSimulacionDto {

    private Integer puntosDisponibles;
    private Integer puntosCanjeables;
    private Integer montoDescuento;
    private boolean puedeCanjear;
    private String mensaje;

}
