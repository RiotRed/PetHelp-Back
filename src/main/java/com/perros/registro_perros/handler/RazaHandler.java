package com.perros.registro_perros.handler;

import com.perros.registro_perros.model.Raza;
import com.perros.registro_perros.service.RazaService;
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
public class RazaHandler {

    private final RazaService razaService;

    public Mono<ServerResponse> getAll(ServerRequest request) {
        Flux<Raza> razas = razaService.listar();
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(razas, Raza.class);
    }

    public Mono<ServerResponse> getById(ServerRequest request) {
        Long id = Long.parseLong(request.pathVariable("id"));
        Mono<Raza> raza = razaService.obtenerPorId(id);
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(raza, Raza.class);
    }

    public Mono<ServerResponse> save(ServerRequest request) {
        Mono<Raza> raza = request.bodyToMono(Raza.class);
        return raza.flatMap(r -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                .body(razaService.registrar(r), Raza.class));
    }

    public Mono<ServerResponse> update(ServerRequest request) {
        Long id = Long.parseLong(request.pathVariable("id"));
        Mono<Raza> raza = request.bodyToMono(Raza.class);
        return raza.flatMap(r -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                .body(razaService.actualizar(id, r), Raza.class));
    }

    public Mono<ServerResponse> delete(ServerRequest request) {
        Long id = Long.parseLong(request.pathVariable("id"));
        return razaService.eliminar(id)
                .then(ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).build());
    }
}
