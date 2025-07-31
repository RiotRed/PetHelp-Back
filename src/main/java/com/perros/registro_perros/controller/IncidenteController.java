package com.perros.registro_perros.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.perros.registro_perros.model.Incidente;
import com.perros.registro_perros.service.IncidenteService;
import com.perros.registro_perros.util.JwtUtil;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/incidentes")
@CrossOrigin(origins = "*")
public class IncidenteController {
    
    @Autowired
    private IncidenteService service;
    
    @Autowired
    private JwtUtil jwtUtil;

    // Público: Obtener todos los incidentes
    @GetMapping
    public Flux<Incidente> listarTodos() {
        return service.listarTodos();
    }

    // Público: Obtener un incidente por ID
    @GetMapping("/{id}")
    public Mono<ResponseEntity<Incidente>> obtenerPorId(@PathVariable Long id) {
        return service.obtenerPorId(id)
                .map(incidente -> ResponseEntity.ok(incidente))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    // Registrador: Reportar un incidente
    @PostMapping
    public Mono<ResponseEntity<Incidente>> reportar(@RequestBody Incidente incidente, @RequestHeader("Authorization") String authHeader) {
        try {
            String token = jwtUtil.extractTokenFromHeader(authHeader);
            Long usuarioId = jwtUtil.extractUserId(token);
            incidente.setUsuarioId(usuarioId);
            return service.registrar(incidente)
                    .map(savedIncidente -> ResponseEntity.ok(savedIncidente))
                    .onErrorReturn(ResponseEntity.badRequest().build());
        } catch (Exception e) {
            return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
        }
    }

    // Registrador: Actualizar incidente
    @PutMapping("/{id}")
    public Mono<ResponseEntity<Incidente>> actualizar(@PathVariable Long id, @RequestBody Incidente incidente, @RequestHeader("Authorization") String authHeader) {
        try {
            String token = jwtUtil.extractTokenFromHeader(authHeader);
            Long usuarioId = jwtUtil.extractUserId(token);
            incidente.setUsuarioId(usuarioId);
            return service.actualizar(id, incidente)
                    .map(updatedIncidente -> ResponseEntity.ok(updatedIncidente))
                    .defaultIfEmpty(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
        }
    }

    // Registrador: Eliminar incidente
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> eliminar(@PathVariable Long id, @RequestHeader("Authorization") String authHeader) {
        try {
            return service.eliminar(id)
                    .then(Mono.just(ResponseEntity.ok().<Void>build()))
                    .defaultIfEmpty(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
        }
    }

    // Público: Obtener estadísticas de incidentes
    @GetMapping("/estadisticas")
    public Mono<ResponseEntity<Map<String, Object>>> obtenerEstadisticas() {
        return service.listarTodos()
                .collectList()
                .map(incidentes -> {
                    Map<String, Object> resultado = new HashMap<>();
                    resultado.put("totalIncidentes", incidentes.size());
                    
                    Map<String, Long> porTipo = incidentes.stream()
                            .filter(incidente -> incidente.getTipo() != null)
                            .collect(java.util.stream.Collectors.groupingBy(
                                    Incidente::getTipo,
                                    java.util.stream.Collectors.counting()
                            ));
                    resultado.put("porTipo", porTipo);
                    
                    Map<String, Long> porEstado = incidentes.stream()
                            .filter(incidente -> incidente.getEstado() != null)
                            .collect(java.util.stream.Collectors.groupingBy(
                                    Incidente::getEstado,
                                    java.util.stream.Collectors.counting()
                            ));
                    resultado.put("porEstado", porEstado);
                    
                    return ResponseEntity.ok(resultado);
                })
                .onErrorReturn(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }

    // Público: Listar incidentes por tipo
    @GetMapping("/tipo")
    public Flux<Incidente> listarPorTipo(@RequestParam String tipo) {
        return service.listarPorTipo(tipo);
    }

    // Público: Listar incidentes por estado
    @GetMapping("/estado")
    public Flux<Incidente> listarPorEstado(@RequestParam String estado) {
        return service.listarPorEstado(estado);
    }

    // Público: Listar incidentes de un perro específico
    @GetMapping("/perro/{perroId}")
    public Flux<Incidente> listarPorPerro(@PathVariable Long perroId) {
        return service.listarPorPerro(perroId);
    }
} 