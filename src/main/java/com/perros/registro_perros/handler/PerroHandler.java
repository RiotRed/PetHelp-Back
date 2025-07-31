package com.perros.registro_perros.handler;

import com.perros.registro_perros.DTO.PerroConUsuarioDTO;
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
        logRequestDetails(request, "Obteniendo perros por usuario");

        String authHeader = request.headers().firstHeader("Authorization");
        Long usuarioId = extraerUsuarioId(authHeader);

        if (usuarioId == null) {
            log.warn("No se pudo obtener el usuarioId del token. Auth header: {}", authHeader);
            // Temporalmente permitir acceso sin autenticación para testing
            log.info("Permitiendo acceso sin autenticación para testing");
            // Usar DTO para manejar usuarioId correctamente
            Flux<PerroConUsuarioDTO> perros = perroService.findPerrosConUsuario();
            return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(perros, PerroConUsuarioDTO.class);
        }

        // Usar DTO para manejar usuarioId correctamente
        Flux<PerroConUsuarioDTO> perros = perroService.findPerrosConUsuario();
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(perros, PerroConUsuarioDTO.class);
    }

    public Mono<ServerResponse> getById(ServerRequest request) {
        Long id = Long.parseLong(request.pathVariable("id"));
        logRequestDetails(request, "Buscando perro con ID: " + id);
        // Usar DTO para manejar usuarioId correctamente
        Mono<Perro> perro = perroService.obtenerPorId(id);
        return perro.flatMap(p -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(mapToDto(p)));
    }

    public Mono<ServerResponse> save(ServerRequest request) {
        logRequestDetails(request, "Guardando perro desde body");
        
        String authHeader = request.headers().firstHeader("Authorization");
        Long usuarioId = extraerUsuarioId(authHeader);
        
        if (usuarioId == null) {
            log.warn("No se pudo obtener el usuarioId del token para guardar. Auth header: {}", authHeader);
            // Temporalmente usar un usuarioId por defecto para testing
            usuarioId = 1L; // Usuario por defecto para testing
            log.info("Usando usuarioId por defecto: {}", usuarioId);
        }
        
        final Long finalUsuarioId = usuarioId;
        Mono<Perro> perro = request.bodyToMono(Perro.class)
                .map(p -> {
                    p.setUsuarioId(finalUsuarioId);
                    return p;
                });
        
        return perro.flatMap(p -> perroService.registrar(p)
                .map(savedPerro -> mapToDto(savedPerro))
                .flatMap(dto -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(dto)));
    }

    public Mono<ServerResponse> update(ServerRequest request) {
        Long id = Long.parseLong(request.pathVariable("id"));
        
        String authHeader = request.headers().firstHeader("Authorization");
        Long usuarioId = extraerUsuarioId(authHeader);
        
        if (usuarioId == null) {
            log.warn("No se pudo obtener el usuarioId del token para actualizar. Auth header: {}", authHeader);
            // Temporalmente usar un usuarioId por defecto para testing
            usuarioId = 1L; // Usuario por defecto para testing
            log.info("Usando usuarioId por defecto: {}", usuarioId);
        }
        
        final Long finalUsuarioId = usuarioId;
        logRequestDetails(request, "Actualizando perro con ID: " + id + " y usuarioId: " + finalUsuarioId);
        Mono<Perro> perro = request.bodyToMono(Perro.class);
        
        return perro.flatMap(p -> perroService.actualizar(id, finalUsuarioId, p)
                .map(updatedPerro -> mapToDto(updatedPerro))
                .flatMap(dto -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(dto)));
    }

    public Mono<ServerResponse> delete(ServerRequest request) {
        Long id = Long.parseLong(request.pathVariable("id"));
        
        String authHeader = request.headers().firstHeader("Authorization");
        Long usuarioId = extraerUsuarioId(authHeader);
        
        if (usuarioId == null) {
            log.warn("No se pudo obtener el usuarioId del token para eliminar. Auth header: {}", authHeader);
            // Temporalmente usar un usuarioId por defecto para testing
            usuarioId = 1L; // Usuario por defecto para testing
            log.info("Usando usuarioId por defecto: {}", usuarioId);
        }
        
        final Long finalUsuarioId = usuarioId;
        logRequestDetails(request, "Eliminando perro con ID: " + id + " y usuarioId: " + finalUsuarioId);
        
        return perroService.eliminarPorUsuario(id, finalUsuarioId)
                .then(ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).build());
    }

    public Mono<ServerResponse> getPerrosConUsuario(ServerRequest request) {
        Flux<PerroConUsuarioDTO> perros = perroService.findPerrosConUsuario();
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(perros, PerroConUsuarioDTO.class);
    }

    private void logRequestDetails(ServerRequest request, String message) {
        String method = request.methodName();
        String path = request.path();
        String ip = request.remoteAddress().map(Object::toString).orElse("IP desconocida");
        log.info("Request {} {} desde {} - {}", method, path, ip, message);
    }

    private Long extraerUsuarioId(String authHeader) {
        log.info("Auth header recibido: {}", authHeader);
        
        if (authHeader == null || authHeader.trim().isEmpty()) {
            log.warn("Header de autorización está vacío o es null");
            return null;
        }
        
        // Intentar diferentes formatos de token
        String token = authHeader.trim();
        
        // Formato 1: "Bearer fake-jwt-token-123"
        if (token.startsWith("Bearer fake-jwt-token-")) {
            try {
                String userIdStr = token.replace("Bearer fake-jwt-token-", "").trim();
                return Long.parseLong(userIdStr);
            } catch (NumberFormatException e) {
                log.warn("No se pudo parsear el userId del token: {}", token);
                return null;
            }
        }
        
        // Formato 2: "Bearer 123" (solo el número)
        if (token.startsWith("Bearer ")) {
            try {
                String userIdStr = token.replace("Bearer ", "").trim();
                return Long.parseLong(userIdStr);
            } catch (NumberFormatException e) {
                log.warn("No se pudo parsear el userId del token: {}", token);
                return null;
            }
        }
        
        // Formato 3: Solo el número sin "Bearer"
        try {
            return Long.parseLong(token);
        } catch (NumberFormatException e) {
            log.warn("No se pudo parsear el userId del token: {}", token);
            return null;
        }
    }
    
    private PerroConUsuarioDTO mapToDto(Perro perro) {
        PerroConUsuarioDTO dto = new PerroConUsuarioDTO();
        dto.setId(perro.getId());
        dto.setNombre(perro.getNombre() != null ? perro.getNombre() : "");
        dto.setDistritoid(perro.getDistritoid());
        dto.setRazaId(perro.getRazaid());
        dto.setTamanio(perro.getTamanio() != null ? perro.getTamanio() : "");
        dto.setComportamiento(perro.getComportamiento() != null ? perro.getComportamiento() : "");
        dto.setColor(perro.getColor() != null ? perro.getColor() : "");
        dto.setGenero(perro.getGenero() != null ? perro.getGenero() : "");
        dto.setEdad(perro.getEdad());
        dto.setVacunado(perro.getVacunado() != null ? perro.getVacunado() : false);
        dto.setEsterilizado(perro.getEsterilizado() != null ? perro.getEsterilizado() : false);
        dto.setUsuarioId(perro.getUsuarioId()); // Mantener el valor real de la BD
        dto.setDireccion(perro.getDireccion() != null ? perro.getDireccion() : "");
        return dto;
    }
}
