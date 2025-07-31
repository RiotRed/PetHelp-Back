package com.perros.registro_perros.handler;

import com.perros.registro_perros.model.Incidente;
import com.perros.registro_perros.service.IncidenteService;
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
public class IncidenteHandler {

    private final IncidenteService incidenteService;

    public Mono<ServerResponse> getAll(ServerRequest request) {
        Flux<Incidente> incidentes = incidenteService.listarTodos();
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(incidentes, Incidente.class);
    }

    public Mono<ServerResponse> getById(ServerRequest request) {
        Long id = Long.parseLong(request.pathVariable("id"));
        Mono<Incidente> incidente = incidenteService.obtenerPorId(id);
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(incidente, Incidente.class);
    }

    public Mono<ServerResponse> save(ServerRequest request) {
        Mono<Incidente> incidente = request.bodyToMono(Incidente.class);
        return incidente.flatMap(i -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                .body(incidenteService.registrar(i), Incidente.class));
    }

    public Mono<ServerResponse> update(ServerRequest request) {
        Long id = Long.parseLong(request.pathVariable("id"));
        Mono<Incidente> incidente = request.bodyToMono(Incidente.class);
        return incidente.flatMap(i -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                .body(incidenteService.actualizar(id, i), Incidente.class));
    }

    public Mono<ServerResponse> delete(ServerRequest request) {
        Long id = Long.parseLong(request.pathVariable("id"));
        return incidenteService.eliminar(id)
                .then(ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).build());
    }
}
