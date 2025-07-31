package com.perros.registro_perros.handler;

import com.perros.registro_perros.model.Distrito;
import com.perros.registro_perros.service.DistritoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class DistritoHandler {

    private final DistritoService distritoService;

    public Mono<ServerResponse> getAll(ServerRequest request) {
        Flux<Distrito> distritos = distritoService.listar();
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(distritos, Distrito.class);
    }

    public Mono<ServerResponse> getById(ServerRequest request) {
        Long id = Long.parseLong(request.pathVariable("id"));
        Mono<Distrito> distrito = distritoService.obtenerPorId(id);
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(distrito, Distrito.class);
    }

    public Mono<ServerResponse> save(ServerRequest request) {
        Mono<Distrito> distrito = request.bodyToMono(Distrito.class);
        return distrito.flatMap(d -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                .body(distritoService.registrar(d), Distrito.class));
    }

    public Mono<ServerResponse> update(ServerRequest request) {
        Long id = Long.parseLong(request.pathVariable("id"));
        Mono<Distrito> distrito = request.bodyToMono(Distrito.class);
        return distrito.flatMap(d -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                .body(distritoService.actualizar(id, d), Distrito.class));
    }

    public Mono<ServerResponse> delete(ServerRequest request) {
        Long id = Long.parseLong(request.pathVariable("id"));
        return distritoService.eliminar(id)
                .then(ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).build());
    }
}
