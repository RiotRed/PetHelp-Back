package com.perros.registro_perros.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.perros.registro_perros.model.Incidente;
import com.perros.registro_perros.service.IncidenteService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/incidente")
@CrossOrigin(origins = "*")
public class IncidenteController {
    
    @Autowired
    private IncidenteService service;

    // Registrador: Reportar un incidente
    @PostMapping
    public Mono<Incidente> reportar(@RequestBody Incidente incidente, @RequestHeader("Authorization") String authHeader) {
        Long usuarioId = extraerUsuarioId(authHeader);
        incidente.setUsuarioId(usuarioId);
        return service.registrar(incidente);
    }

    // Registrador: Listar todos los incidentes
    @GetMapping
    public Flux<Incidente> listarTodos() {
        return service.listarTodos();
    }

    // Registrador: Listar incidentes por tipo
    @GetMapping("/tipo/{tipo}")
    public Flux<Incidente> listarPorTipo(@PathVariable String tipo) {
        return service.listarPorTipo(tipo);
    }

    // Registrador: Listar incidentes por estado
    @GetMapping("/estado/{estado}")
    public Flux<Incidente> listarPorEstado(@PathVariable String estado) {
        return service.listarPorEstado(estado);
    }

    // Registrador: Listar incidentes de un perro específico
    @GetMapping("/perro/{perroId}")
    public Flux<Incidente> listarPorPerro(@PathVariable Long perroId) {
        return service.listarPorPerro(perroId);
    }

    // Registrador: Obtener incidente por ID
    @GetMapping("/{id}")
    public Mono<Incidente> obtenerPorId(@PathVariable Long id) {
        return service.obtenerPorId(id);
    }

    // Registrador: Actualizar estado de incidente
    @PutMapping("/{id}")
    public Mono<Incidente> actualizar(@PathVariable Long id, @RequestBody Incidente incidente, @RequestHeader("Authorization") String authHeader) {
        Long usuarioId = extraerUsuarioId(authHeader);
        incidente.setUsuarioId(usuarioId);
        return service.actualizar(id, incidente);
    }

    // Registrador: Eliminar incidente
    @DeleteMapping("/{id}")
    public Mono<Void> eliminar(@PathVariable Long id, @RequestHeader("Authorization") String authHeader) {
        return service.eliminar(id);
    }

    private Long extraerUsuarioId(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer fake-jwt-token-")) {
            throw new RuntimeException("Token inválido");
        }
        String idStr = authHeader.replace("Bearer fake-jwt-token-", "").trim();
        return Long.parseLong(idStr);
    }
} 