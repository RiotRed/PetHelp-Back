package com.perros.registro_perros.controller;

import org.springframework.beans.factory.annotation.Autowired;
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
    public Mono<Incidente> obtenerPorId(@PathVariable Long id) {
        return service.obtenerPorId(id);
    }

    // Registrador: Reportar un incidente
    @PostMapping
    public Mono<Incidente> reportar(@RequestBody Incidente incidente, @RequestHeader("Authorization") String authHeader) {
        String token = jwtUtil.extractTokenFromHeader(authHeader);
        Long usuarioId = jwtUtil.extractUserId(token);
        incidente.setUsuarioId(usuarioId);
        return service.registrar(incidente);
    }

    // Registrador: Actualizar incidente
    @PutMapping("/{id}")
    public Mono<Incidente> actualizar(@PathVariable Long id, @RequestBody Incidente incidente, @RequestHeader("Authorization") String authHeader) {
        String token = jwtUtil.extractTokenFromHeader(authHeader);
        Long usuarioId = jwtUtil.extractUserId(token);
        incidente.setUsuarioId(usuarioId);
        return service.actualizar(id, incidente);
    }

    // Registrador: Eliminar incidente
    @DeleteMapping("/{id}")
    public Mono<Void> eliminar(@PathVariable Long id, @RequestHeader("Authorization") String authHeader) {
        return service.eliminar(id);
    }

    // Público: Obtener estadísticas de incidentes
    @GetMapping("/estadisticas")
    public Mono<Map<String, Object>> obtenerEstadisticas() {
        return service.listarTodos()
                .collectList()
                .map(incidentes -> {
                    Map<String, Object> resultado = new HashMap<>();
                    resultado.put("totalIncidentes", incidentes.size());
                    
                    Map<String, Long> porTipo = incidentes.stream()
                            .collect(java.util.stream.Collectors.groupingBy(
                                    Incidente::getTipo,
                                    java.util.stream.Collectors.counting()
                            ));
                    resultado.put("porTipo", porTipo);
                    
                    Map<String, Long> porEstado = incidentes.stream()
                            .collect(java.util.stream.Collectors.groupingBy(
                                    Incidente::getEstado,
                                    java.util.stream.Collectors.counting()
                            ));
                    resultado.put("porEstado", porEstado);
                    
                    return resultado;
                });
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