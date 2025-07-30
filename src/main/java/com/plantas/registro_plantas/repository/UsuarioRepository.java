package com.plantas.registro_plantas.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.plantas.registro_plantas.model.Usuario;

import reactor.core.publisher.Mono;

public interface UsuarioRepository extends ReactiveCrudRepository<Usuario, Long> {
    Mono<Usuario> findByEmail(String email);
}
