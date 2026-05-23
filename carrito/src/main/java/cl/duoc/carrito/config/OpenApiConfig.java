package cl.duoc.carrito.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenApi(){
        return new OpenAPI()
                .info(new Info()
                        .title("Microservicio de Carrito API")
                        .version("1.0.0")
                        .description("Documentación interactiva de los endpoints de carrito"));
    }

    @Bean
    public GroupedOpenApi carritoApi() {
        return GroupedOpenApi.builder()
                .group("1. Módulo de Carrito")
                .pathsToMatch("/api/v1/carts/**")
                .build();
    }

}
