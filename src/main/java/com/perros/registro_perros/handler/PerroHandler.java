package com.perros.registro_perros.handler;

import com.perros.registro_perros.model.Perro;
import com.perros.registro_perros.service.PerroService;
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
public class PerroHandler {

    private final PerroService perroService;

    public Mono<ServerResponse> getAll(ServerRequest request) {
        logRequestDetails(request, "Obteniendo todos los perros");
        Flux<Perro> perros = perroService.listar();
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(perros, Perro.class);
    }

    public Mono<ServerResponse> getById(ServerRequest request) {
        Long id = Long.parseLong(request.pathVariable("id"));
        logRequestDetails(request, "Buscando perro con ID: " + id);
        Mono<Perro> perro = perroService.obtenerPorId(id);
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(perro, Perro.class);
    }

    public Mono<ServerResponse> save(ServerRequest request) {
        logRequestDetails(request, "Guardando perro desde body");
        Mono<Perro> perro = request.bodyToMono(Perro.class);
        return perro.flatMap(p -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                .body(perroService.registrar(p), Perro.class));
    }

    public Mono<ServerResponse> update(ServerRequest request) {
        Long id = Long.parseLong(request.pathVariable("id"));
        // Si necesitas usuarioId, obténlo del request (por ejemplo, query param o path variable)
        Long usuarioId = request.queryParam("usuarioId").map(Long::parseLong).orElse(null);
        logRequestDetails(request, "Actualizando perro con ID: " + id + " y usuarioId: " + usuarioId);
        Mono<Perro> perro = request.bodyToMono(Perro.class);
        if (usuarioId == null) {
            return ServerResponse.badRequest().bodyValue("usuarioId es requerido para actualizar");
        }
        return perro.flatMap(p -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                .body(perroService.actualizar(id, usuarioId, p), Perro.class));
    }

    public Mono<ServerResponse> delete(ServerRequest request) {
        Long id = Long.parseLong(request.pathVariable("id"));
        // Si necesitas usuarioId, obténlo del request (por ejemplo, query param o path variable)
        Long usuarioId = request.queryParam("usuarioId").map(Long::parseLong).orElse(null);
        logRequestDetails(request, "Eliminando perro con ID: " + id + " y usuarioId: " + usuarioId);
        if (usuarioId == null) {
            return ServerResponse.badRequest().bodyValue("usuarioId es requerido para eliminar");
        }
        return perroService.eliminarPorUsuario(id, usuarioId)
                .then(ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).build());
    }

    private void logRequestDetails(ServerRequest request, String message) {
        String method = request.methodName();
        String path = request.path();
        String ip = request.remoteAddress().map(Object::toString).orElse("IP desconocida");
        log.info("Request {} {} desde {} - {}", method, path, ip, message);
    }
}
