package com.perros.registro_perros.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.perros.registro_perros.model.Perro;
import com.perros.registro_perros.service.PerroService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/perros")
@CrossOrigin(origins = "*")
public class PerroController {
    
    @Autowired
    private PerroService service;

    // Registrador: Registrar perros y sus dueños
    @PostMapping
    public Mono<Perro> registrar(@RequestBody Perro perro, @RequestHeader("Authorization") String authHeader) {
        Long usuarioId = extraerUsuarioId(authHeader);
        perro.setUsuarioId(usuarioId);
        return service.registrar(perro);
    }

    // Registrador: Listar perros registrados por el usuario
    @GetMapping
    public Flux<Perro> listar(@RequestHeader("Authorization") String authHeader) {
        Long usuarioId = extraerUsuarioId(authHeader);
        return service.listarPorUsuario(usuarioId);
    }

    // Público: Buscar perros por dueño
    @GetMapping("/dueno/{dueno}")
    public Flux<Perro> buscarPorDueno(@PathVariable String dueno) {
        return service.listar()
                .filter(perro -> perro.getDueño().toLowerCase().contains(dueno.toLowerCase()));
    }

    // Público: Buscar perros por raza
    @GetMapping("/raza/{razaId}")
    public Flux<Perro> buscarPorRaza(@PathVariable Integer razaId) {
        return service.listar()
                .filter(perro -> perro.getRazaid() == razaId);
    }

    // Público: Buscar perros por tamaño
    @GetMapping("/tamaño/{tamaño}")
    public Flux<Perro> buscarPorTamaño(@PathVariable String tamaño) {
        return service.listar()
                .filter(perro -> perro.getTamaño().equalsIgnoreCase(tamaño));
    }

    // Público: Buscar perros por comportamiento
    @GetMapping("/comportamiento/{comportamiento}")
    public Flux<Perro> buscarPorComportamiento(@PathVariable String comportamiento) {
        return service.listar()
                .filter(perro -> perro.getComportamiento().equalsIgnoreCase(comportamiento));
    }

    // Público: Buscar perros por distrito
    @GetMapping("/distrito/{distritoId}")
    public Flux<Perro> buscarPorDistrito(@PathVariable Integer distritoId) {
        return service.listar()
                .filter(perro -> perro.getDistritoid() == distritoId);
    }

    // Público: Obtener densidad canina por distrito
    @GetMapping("/densidad/distrito/{distritoId}")
    public Mono<Long> obtenerDensidadCanina(@PathVariable Integer distritoId) {
        return service.listar()
                .filter(perro -> perro.getDistritoid() == distritoId)
                .count();
    }

    // Registrador: Actualizar perro
    @PutMapping("/{id}")
    public Mono<Perro> actualizar(@PathVariable Long id, @RequestBody Perro perro, @RequestHeader("Authorization") String authHeader) {
        Long usuarioId = extraerUsuarioId(authHeader);
        return service.actualizar(id, usuarioId, perro);
    }

    // Registrador: Eliminar perro
    @DeleteMapping("/{id}")
    public Mono<Void> eliminar(@PathVariable Long id, @RequestHeader("Authorization") String authHeader) {
        Long usuarioId = extraerUsuarioId(authHeader);
        return service.eliminarPorUsuario(id, usuarioId);
    }

    // Público: Obtener perro por ID
    @GetMapping("/{id}")
    public Mono<Perro> obtenerPorId(@PathVariable Long id) {
        return service.obtenerPorId(id);
    }

    private Long extraerUsuarioId(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer fake-jwt-token-")) {
            throw new RuntimeException("Token inválido");
        }
        String idStr = authHeader.replace("Bearer fake-jwt-token-", "").trim();
        return Long.parseLong(idStr);
    }
} 