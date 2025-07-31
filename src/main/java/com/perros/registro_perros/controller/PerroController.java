package com.perros.registro_perros.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.perros.registro_perros.DTO.PerroConUsuarioDTO;
import com.perros.registro_perros.model.Perro;
import com.perros.registro_perros.service.PerroService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;

@RestController
@RequestMapping("/api/perros")
@CrossOrigin(origins = "*")
public class PerroController {
    
    @Autowired
    private PerroService service;

    @PostMapping
    public Mono<Perro> registrar(@RequestBody Perro perro, @RequestHeader("Authorization") String authHeader) {
        Long usuarioId = extraerUsuarioId(authHeader);
        perro.setUsuarioId(usuarioId);
        return service.registrar(perro);
    }

    @GetMapping
    public Flux<Perro> listar(@RequestHeader("Authorization") String authHeader) {
        Long usuarioId = extraerUsuarioId(authHeader);
        return service.listarPorUsuario(usuarioId);
    }

    @GetMapping("/con-usuario")
    public Flux<PerroConUsuarioDTO> listarConUsuario() {
        return service.findPerrosConUsuario();
    }

    @GetMapping("/raza/{razaId}")
    public Flux<Perro> buscarPorRaza(@PathVariable Integer razaId) {
        return service.listar()
                .filter(perro -> Objects.equals(perro.getRazaid(), Long.valueOf(razaId)));
    }

    @GetMapping("/tamaño/{tamaño}")
    public Flux<Perro> buscarPorTamanio(@PathVariable String tamaño) {
        return service.listar()
                .filter(perro -> perro.getTamanio() != null && perro.getTamanio().equalsIgnoreCase(tamaño));
    }

    @GetMapping("/comportamiento/{comportamiento}")
    public Flux<Perro> buscarPorComportamiento(@PathVariable String comportamiento) {
        return service.listar()
                .filter(perro -> perro.getComportamiento() != null && perro.getComportamiento().equalsIgnoreCase(comportamiento));
    }

    @GetMapping("/distrito/{distritoId}")
    public Flux<Perro> buscarPorDistrito(@PathVariable Integer distritoId) {
        return service.listar()
                .filter(perro -> Objects.equals(perro.getDistritoid(), Long.valueOf(distritoId)));
    }

    @GetMapping("/densidad/distrito/{distritoId}")
    public Mono<Long> obtenerDensidadCanina(@PathVariable Integer distritoId) {
        return service.listar()
                .filter(perro -> Objects.equals(perro.getDistritoid(), Long.valueOf(distritoId)))
                .count();
    }

    @PutMapping("/{id}")
    public Mono<Perro> actualizar(@PathVariable Long id, @RequestBody Perro perro, @RequestHeader("Authorization") String authHeader) {
        Long usuarioId = extraerUsuarioId(authHeader);
        return service.actualizar(id, usuarioId, perro);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> eliminar(@PathVariable Long id, @RequestHeader("Authorization") String authHeader) {
        Long usuarioId = extraerUsuarioId(authHeader);
        return service.eliminarPorUsuario(id, usuarioId);
    }

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
