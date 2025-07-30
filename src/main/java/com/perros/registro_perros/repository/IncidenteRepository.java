package com.perros.registro_perros.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import com.perros.registro_perros.model.Incidente;

import reactor.core.publisher.Flux;

@Repository
public interface IncidenteRepository extends ReactiveCrudRepository<Incidente, Long> {
    Flux<Incidente> findByPerroId(Long perroId);
    Flux<Incidente> findByTipo(String tipo);
    Flux<Incidente> findByEstado(String estado);
} 