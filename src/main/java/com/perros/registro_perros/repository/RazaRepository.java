package com.perros.registro_perros.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import com.perros.registro_perros.model.Raza;

import reactor.core.publisher.Flux;

@Repository
public interface RazaRepository extends ReactiveCrudRepository<Raza, Long> {
    Flux<Raza> findByNombreContainingIgnoreCase(String nombre);
} 