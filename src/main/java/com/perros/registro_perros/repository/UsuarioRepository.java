package com.perros.registro_perros.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.perros.registro_perros.model.Usuario;

import reactor.core.publisher.Mono;

public interface UsuarioRepository extends ReactiveCrudRepository<Usuario, Long> {
    Mono<Usuario> findByEmail(String email);
}
