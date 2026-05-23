package cl.duoc.carrito.dto.remote;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter             @Setter
@AllArgsConstructor @NoArgsConstructor
public class LoginDto {

    private Long id;
    private String username;
    private RolDto rol;

}
