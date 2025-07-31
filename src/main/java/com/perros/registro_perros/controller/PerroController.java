package com.perros.registro_perros.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.perros.registro_perros.model.Perro;
import com.perros.registro_perros.service.PerroService;
import com.perros.registro_perros.util.JwtUtil;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.Map;
import java.util.HashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/perros")
@CrossOrigin(origins = "*")
public class PerroController {
    
    private static final Logger logger = LoggerFactory.getLogger(PerroController.class);
    
    @Autowired
    private PerroService service;
    
    @Autowired
    private JwtUtil jwtUtil;

    // Público: Obtener todos los perros
    @GetMapping
    public Flux<Perro> listarTodos() {
        logger.info("Solicitando lista de todos los perros");
        return service.listar()
                .doOnNext(perro -> logger.debug("Perro encontrado: {}", perro.getNombre()))
                .doOnError(error -> logger.error("Error al listar perros: {}", error.getMessage()))
                .onErrorResume(e -> {
                    logger.error("Error crítico al listar perros", e);
                    return Flux.empty();
                });
    }

    // Público: Obtener un perro por ID
    @GetMapping("/{id}")
    public Mono<ResponseEntity<Perro>> obtenerPorId(@PathVariable Long id) {
        logger.info("Solicitando perro con ID: {}", id);
        return service.obtenerPorId(id)
                .map(perro -> {
                    logger.info("Perro encontrado: {}", perro.getNombre());
                    return ResponseEntity.ok(perro);
                })
                .defaultIfEmpty(ResponseEntity.notFound().build())
                .doOnError(error -> logger.error("Error al obtener perro con ID {}: {}", id, error.getMessage()));
    }

    // Registrador: Registrar perros y sus dueños
    @PostMapping
    public Mono<ResponseEntity<Perro>> registrar(@RequestBody Perro perro, @RequestHeader("Authorization") String authHeader) {
        try {
            String token = jwtUtil.extractTokenFromHeader(authHeader);
            Long usuarioId = jwtUtil.extractUserId(token);
            perro.setUsuarioId(usuarioId);
            logger.info("Registrando nuevo perro: {}", perro.getNombre());
            return service.registrar(perro)
                    .map(savedPerro -> {
                        logger.info("Perro registrado exitosamente: {}", savedPerro.getNombre());
                        return ResponseEntity.ok(savedPerro);
                    })
                    .onErrorReturn(ResponseEntity.badRequest().build());
        } catch (Exception e) {
            logger.error("Error al registrar perro", e);
            return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
        }
    }

    // Registrador: Actualizar perro
    @PutMapping("/{id}")
    public Mono<ResponseEntity<Perro>> actualizar(@PathVariable Long id, @RequestBody Perro perro, @RequestHeader("Authorization") String authHeader) {
        try {
            String token = jwtUtil.extractTokenFromHeader(authHeader);
            Long usuarioId = jwtUtil.extractUserId(token);
            logger.info("Actualizando perro con ID: {}", id);
            return service.actualizar(id, usuarioId, perro)
                    .map(updatedPerro -> {
                        logger.info("Perro actualizado exitosamente: {}", updatedPerro.getNombre());
                        return ResponseEntity.ok(updatedPerro);
                    })
                    .defaultIfEmpty(ResponseEntity.notFound().build());
        } catch (Exception e) {
            logger.error("Error al actualizar perro con ID: {}", id, e);
            return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
        }
    }

    // Registrador: Eliminar perro
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> eliminar(@PathVariable Long id, @RequestHeader("Authorization") String authHeader) {
        try {
            String token = jwtUtil.extractTokenFromHeader(authHeader);
            Long usuarioId = jwtUtil.extractUserId(token);
            logger.info("Eliminando perro con ID: {}", id);
            return service.eliminarPorUsuario(id, usuarioId)
                    .then(Mono.just(ResponseEntity.ok().<Void>build()))
                    .defaultIfEmpty(ResponseEntity.notFound().build());
        } catch (Exception e) {
            logger.error("Error al eliminar perro con ID: {}", id, e);
            return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
        }
    }

    // Público: Buscar perros por dueño (usando usuarioId)
    @GetMapping("/buscar-dueno")
    public Flux<Perro> buscarPorDueno(@RequestParam String nombre) {
        logger.info("Buscando perros por dueño: {}", nombre);
        // Por ahora, retornamos todos los perros ya que necesitamos implementar
        // la búsqueda por nombre de dueño a través de la relación con Usuario
        return service.listar();
    }

    // Público: Buscar perros por raza
    @GetMapping("/raza")
    public Flux<Perro> buscarPorRaza(@RequestParam String raza) {
        logger.info("Buscando perros por raza: {}", raza);
        return service.listar()
                .filter(perro -> perro.getRazaid() != null && 
                       perro.getRazaid().toString().equals(raza));
    }

    // Público: Buscar perros por tamaño
    @GetMapping("/tamaño")
    public Flux<Perro> buscarPorTamaño(@RequestParam String tamaño) {
        logger.info("Buscando perros por tamaño: {}", tamaño);
        return service.listar()
                .filter(perro -> perro.getTamanio() != null && 
                       perro.getTamanio().equalsIgnoreCase(tamaño));
    }

    // Público: Buscar perros por comportamiento
    @GetMapping("/comportamiento")
    public Flux<Perro> buscarPorComportamiento(@RequestParam String comportamiento) {
        logger.info("Buscando perros por comportamiento: {}", comportamiento);
        return service.listar()
                .filter(perro -> perro.getComportamiento() != null && 
                       perro.getComportamiento().equalsIgnoreCase(comportamiento));
    }

    // Público: Buscar perros por ubicación
    @GetMapping("/ubicacion")
    public Flux<Perro> buscarPorUbicacion(@RequestParam String ubicacion) {
        logger.info("Buscando perros por ubicación: {}", ubicacion);
        return service.listar()
                .filter(perro -> perro.getDireccion() != null && 
                       perro.getDireccion().toLowerCase().contains(ubicacion.toLowerCase()));
    }

    // Público: Obtener mapa de densidad canina
    @GetMapping("/densidad")
    public Mono<ResponseEntity<Map<String, Object>>> obtenerMapaDensidad() {
        logger.info("Solicitando mapa de densidad canina");
        return service.listar()
                .collectList()
                .map(perros -> {
                    Map<String, Object> resultado = new HashMap<>();
                    Map<Long, Long> densidad = perros.stream()
                            .filter(perro -> perro.getDistritoid() != null)
                            .collect(java.util.stream.Collectors.groupingBy(
                                    Perro::getDistritoid,
                                    java.util.stream.Collectors.counting()
                            ));
                    resultado.put("densidadPorDistrito", densidad);
                    logger.info("Mapa de densidad generado con {} perros", perros.size());
                    return ResponseEntity.ok(resultado);
                })
                .onErrorReturn(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }

    // Público: Obtener estadísticas generales
    @GetMapping("/estadisticas")
    public Mono<ResponseEntity<Map<String, Object>>> obtenerEstadisticasGenerales() {
        logger.info("Solicitando estadísticas generales");
        return service.listar()
                .collectList()
                .map(perros -> {
                    Map<String, Object> resultado = new HashMap<>();
                    resultado.put("totalPerros", perros.size());
                    
                    // Estadísticas por tamaño (usando datos reales de la BD)
                    Map<String, Long> porTamaño = perros.stream()
                            .filter(perro -> perro.getTamanio() != null)
                            .collect(java.util.stream.Collectors.groupingBy(
                                    Perro::getTamanio,
                                    java.util.stream.Collectors.counting()
                            ));
                    resultado.put("porTamaño", porTamaño);
                    
                    // Estadísticas por comportamiento (usando datos reales de la BD)
                    Map<String, Long> porComportamiento = perros.stream()
                            .filter(perro -> perro.getComportamiento() != null)
                            .collect(java.util.stream.Collectors.groupingBy(
                                    Perro::getComportamiento,
                                    java.util.stream.Collectors.counting()
                            ));
                    resultado.put("porComportamiento", porComportamiento);
                    
                    // Estadísticas por género
                    Map<String, Long> porGenero = perros.stream()
                            .filter(perro -> perro.getGenero() != null)
                            .collect(java.util.stream.Collectors.groupingBy(
                                    Perro::getGenero,
                                    java.util.stream.Collectors.counting()
                            ));
                    resultado.put("porGenero", porGenero);
                    
                    // Estadísticas por color
                    Map<String, Long> porColor = perros.stream()
                            .filter(perro -> perro.getColor() != null)
                            .collect(java.util.stream.Collectors.groupingBy(
                                    Perro::getColor,
                                    java.util.stream.Collectors.counting()
                            ));
                    resultado.put("porColor", porColor);
                    
                    // Estadísticas por estado de vacunación
                    Map<String, Long> porVacunacion = perros.stream()
                            .filter(perro -> perro.getVacunado() != null)
                            .collect(java.util.stream.Collectors.groupingBy(
                                    perro -> perro.getVacunado() ? "Vacunado" : "No vacunado",
                                    java.util.stream.Collectors.counting()
                            ));
                    resultado.put("porVacunacion", porVacunacion);
                    
                    // Estadísticas por estado de esterilización
                    Map<String, Long> porEsterilizacion = perros.stream()
                            .filter(perro -> perro.getEsterilizado() != null)
                            .collect(java.util.stream.Collectors.groupingBy(
                                    perro -> perro.getEsterilizado() ? "Esterilizado" : "No esterilizado",
                                    java.util.stream.Collectors.counting()
                            ));
                    resultado.put("porEsterilizacion", porEsterilizacion);
                    
                    logger.info("Estadísticas generadas para {} perros", perros.size());
                    return ResponseEntity.ok(resultado);
                })
                .onErrorReturn(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }
} 