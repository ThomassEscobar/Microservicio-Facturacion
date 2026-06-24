package com.consultoria.facturacion.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Facturación - Consultoría")
                        .version("1.0.0")
                        .description("Microservicio financiero encargado de la emisión de cobros, reportes de ingresos y estructuración de comprobantes PDF.")
                        .contact(new Contact()
                                .name("Soporte Financiero")
                                .email("financiero@consultoria.com")));
    }
}