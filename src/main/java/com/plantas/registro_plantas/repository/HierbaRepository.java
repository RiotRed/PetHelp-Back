package com.plantas.registro_plantas.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.plantas.registro_plantas.model.Hierba;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface HierbaRepository extends ReactiveCrudRepository<Hierba, Long> {
    Flux<Hierba> findAllByUsuarioId(Long usuarioId);
    Mono<Hierba> findByIdAndUsuarioId(Long id, Long usuarioId);
}
