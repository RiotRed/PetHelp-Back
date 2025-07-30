package com.perros.registro_perros.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.perros.registro_perros.model.Perro;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PerroRepository extends ReactiveCrudRepository<Perro, Long> {
    Flux<Perro> findAllByUsuarioId(Long usuarioId);
    Mono<Perro> findByIdAndUsuarioId(Long id, Long usuarioId);
}
