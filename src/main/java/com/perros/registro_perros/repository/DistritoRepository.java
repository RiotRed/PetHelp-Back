package com.perros.registro_perros.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import com.perros.registro_perros.model.Distrito;

import reactor.core.publisher.Flux;

@Repository
public interface DistritoRepository extends ReactiveCrudRepository<Distrito, Long> {
    Flux<Distrito> findByNombreContainingIgnoreCase(String nombre);
} 