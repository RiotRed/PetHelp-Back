package com.perros.registro_perros.router;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.perros.registro_perros.handler.RazaHandler;

@Configuration
public class RazaRouter {
    private static final String PATH_PLURAL = "/api/razas";
    private static final String PATH_SINGULAR = "/api/raza";

    @Bean
    RouterFunction<ServerResponse> razasRouter(RazaHandler handler) {
        return RouterFunctions.route()
                // Rutas plurales
                .GET(PATH_PLURAL, handler::getAll)
                .GET(PATH_PLURAL + "/", handler::getAll)
                .GET(PATH_PLURAL + "/{id}", handler::getById)
                .GET(PATH_PLURAL + "/{id}/", handler::getById)
                .POST(PATH_PLURAL, handler::save)
                .POST(PATH_PLURAL + "/", handler::save)
                .PUT(PATH_PLURAL + "/{id}", handler::update)
                .PUT(PATH_PLURAL + "/{id}/", handler::update)
                .DELETE(PATH_PLURAL + "/{id}", handler::delete)
                .DELETE(PATH_PLURAL + "/{id}/", handler::delete)
                // Rutas singulares
                .GET(PATH_SINGULAR, handler::getAll)
                .GET(PATH_SINGULAR + "/", handler::getAll)
                .GET(PATH_SINGULAR + "/{id}", handler::getById)
                .GET(PATH_SINGULAR + "/{id}/", handler::getById)
                .POST(PATH_SINGULAR, handler::save)
                .POST(PATH_SINGULAR + "/", handler::save)
                .PUT(PATH_SINGULAR + "/{id}", handler::update)
                .PUT(PATH_SINGULAR + "/{id}/", handler::update)
                .DELETE(PATH_SINGULAR + "/{id}", handler::delete)
                .DELETE(PATH_SINGULAR + "/{id}/", handler::delete)
                .build();
    }
}
