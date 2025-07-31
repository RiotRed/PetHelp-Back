package com.perros.registro_perros.handler;

import com.perros.registro_perros.model.Usuario;
import com.perros.registro_perros.service.UsuarioService;
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
public class UsuarioHandler {

    private final UsuarioService usuarioService;

    public Mono<ServerResponse> getAll(ServerRequest request) {
        Flux<Usuario> usuarios = usuarioService.findAll();
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(usuarios, Usuario.class);
    }

    public Mono<ServerResponse> getById(ServerRequest request) {
        Long id = Long.parseLong(request.pathVariable("id"));
        Mono<Usuario> usuario = usuarioService.findById(id);
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(usuario, Usuario.class);
    }

    public Mono<ServerResponse> save(ServerRequest request) {
        Mono<Usuario> usuario = request.bodyToMono(Usuario.class);
        return usuario.flatMap(u -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                .body(usuarioService.save(u), Usuario.class));
    }

    public Mono<ServerResponse> update(ServerRequest request) {
        Long id = Long.parseLong(request.pathVariable("id"));
        Mono<Usuario> usuarioMono = request.bodyToMono(Usuario.class);
        return usuarioMono.flatMap(u -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                .body(usuarioService.update(id, u), Usuario.class));
    }

    public Mono<ServerResponse> delete(ServerRequest request) {
        Long id = Long.parseLong(request.pathVariable("id"));
        return usuarioService.delete(id)
                .then(ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).build());
    }
}
