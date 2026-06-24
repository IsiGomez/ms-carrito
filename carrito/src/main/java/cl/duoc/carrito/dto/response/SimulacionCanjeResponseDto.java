package cl.duoc.carrito.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter             @Setter
@AllArgsConstructor @NoArgsConstructor
public class SimulacionCanjeResponseDto {

    private Integer puntosDisponibles;
    private Integer puntosCanjeables;
    private Integer montoDescuento;
    private boolean puedeCanjear;

    private Integer totalCarritoActual;
    private Integer totalCarritoConDescuento;
    private String mensaje;
}
