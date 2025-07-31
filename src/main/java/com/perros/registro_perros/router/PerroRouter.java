package com.perros.registro_perros.router;

import com.perros.registro_perros.handler.PerroHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class PerroRouter {

    @Bean
    public RouterFunction<ServerResponse> perroRoutes(PerroHandler handler) {
        return route(GET("/api/perros"), handler::getAll)
                .andRoute(GET("/api/perros/"), handler::getAll)
                .andRoute(GET("/api/perro"), handler::getAll)
                .andRoute(GET("/api/perro/"), handler::getAll)
                .andRoute(GET("/api/perros/{id}"), handler::getById)
                .andRoute(GET("/api/perros/{id}/"), handler::getById)
                .andRoute(GET("/api/perro/{id}"), handler::getById)
                .andRoute(GET("/api/perro/{id}/"), handler::getById)
                .andRoute(POST("/api/perros"), handler::save)
                .andRoute(POST("/api/perros/"), handler::save)
                .andRoute(POST("/api/perro"), handler::save)
                .andRoute(POST("/api/perro/"), handler::save)
                .andRoute(PUT("/api/perros/{id}"), handler::update)
                .andRoute(PUT("/api/perros/{id}/"), handler::update)
                .andRoute(PUT("/api/perro/{id}"), handler::update)
                .andRoute(PUT("/api/perro/{id}/"), handler::update)
                .andRoute(DELETE("/api/perros/{id}"), handler::delete)
                .andRoute(DELETE("/api/perros/{id}/"), handler::delete)
                .andRoute(DELETE("/api/perro/{id}"), handler::delete)
                .andRoute(DELETE("/api/perro/{id}/"), handler::delete)
                .andRoute(GET("/api/perros-con-usuario"), handler::getPerrosConUsuario)
                .andRoute(GET("/api/perros-con-usuario/"), handler::getPerrosConUsuario);
    }
}

