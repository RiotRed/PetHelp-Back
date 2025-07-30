package com.plantas.registro_plantas.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.plantas.registro_plantas.model.Hierba;
import com.plantas.registro_plantas.service.HierbaService;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@RestController
@RequestMapping("/api/hierbas")
@RequiredArgsConstructor
public class HierbaController {
    
    private final HierbaService service;


    @GetMapping
    public Flux<Hierba> listar(@RequestHeader("Authorization") String authHeader) {
        Long usuarioId = extraerUsuarioId(authHeader);
        return service.listarPorUsuario(usuarioId);
    }

    @PostMapping
    public Mono<Hierba> registrar(@RequestBody Hierba hierba, @RequestHeader("Authorization") String authHeader) {
        Long usuarioId = extraerUsuarioId(authHeader);
        hierba.setUsuarioId(usuarioId);
        return service.registrar(hierba);
    }

    @PutMapping("/{id}")
    public Mono<Hierba> actualizar(@PathVariable Long id, @RequestBody Hierba hierba, @RequestHeader("Authorization") String authHeader) {
        Long usuarioId = extraerUsuarioId(authHeader);
        return service.actualizar(id, usuarioId, hierba);
    }

    @GetMapping("/{id}")
    public Mono<Void> eliminar(@PathVariable Long id, @RequestHeader("Authorization") String authHeader) {
        Long usuarioId = extraerUsuarioId(authHeader);
        return service.eliminarPorUsuario(id, usuarioId);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> eliminar(@PathVariable Long id) {
        return service.eliminar(id);
    }
    
    private Long extraerUsuarioId(String authHeader) {
        // authHeader: "Bearer fake-jwt-token-123"
        if (authHeader == null || !authHeader.startsWith("Bearer fake-jwt-token-")) {
            throw new RuntimeException("Token inválido");
        }
        String idStr = authHeader.replace("Bearer fake-jwt-token-", "").trim();
        return Long.parseLong(idStr);
    }
}
