package com.example.demo.application.configs;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI(){
        return new OpenAPI()
                .info(new Info()
                        .title("API farmacia QuickPharma")
                        .description("API para la administración de ventas de medicamentos QuickPharma. Esta API permite generar facturas en PDF y enviarlas por correo electrónico, realizar la lectura de fórmulas médicas recibidas en imagen y registrar pacientes.")
                        .version("1.0")
                        .contact((new Contact()
                                .name("Mauricio Guerra; Eddie Serna; Jorge Rojas")
                                .url("https://github.com/maurogebe; https://github.com/eddieruiz7; https://github.com/Jjorgerojas")
                                .email("maurogebe@gmail.com; esernaruiz3@gmail.com; Jjorgerojas@gmail.com"))));
    }
}
