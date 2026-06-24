package cl.duoc.carrito.dto.remote;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter             @Setter
@AllArgsConstructor @NoArgsConstructor
public class PromocionDto {

    private Long id;
    private String codigo;
    private Double descuento;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private Boolean acumulable;

    public boolean isVigente() {
        LocalDate hoy = LocalDate.now();
        return fechaInicio != null && fechaFin != null
                && !hoy.isBefore(fechaInicio)
                && !hoy.isAfter(fechaFin);
    }

}
