package cl.duoc.carrito.service.apis;

import cl.duoc.carrito.dto.remote.LoginDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "usuarios")
public interface UsuariosClient {

    @GetMapping("/api/v1/logins/{id}/rol")
    LoginDto getRol(@PathVariable Long id);

}
