package com.perros.registro_perros.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.perros.registro_perros.model.Incidente;
import com.perros.registro_perros.repository.IncidenteRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.time.LocalDateTime;

@Service
public class IncidenteService {

    @Autowired
    private IncidenteRepository repository;

    public Flux<Incidente> listarTodos() {
        return repository.findAll();
    }

    public Flux<Incidente> listarPorPerro(Long perroId) {
        return repository.findByPerroId(perroId);
    }

    public Flux<Incidente> listarPorTipo(String tipo) {
        return repository.findByTipo(tipo);
    }

    public Flux<Incidente> listarPorEstado(String estado) {
        return repository.findByEstado(estado);
    }

    public Mono<Incidente> registrar(Incidente incidente) {
        incidente.setFechaReporte(LocalDateTime.now());
        return repository.save(incidente);
    }

    public Mono<Incidente> actualizar(Long id, Incidente incidente) {
        return repository.findById(id)
                .flatMap(existing -> {
                    incidente.setId(id);
                    return repository.save(incidente);
                });
    }

    public Mono<Void> eliminar(Long id) {
        return repository.deleteById(id);
    }

    public Mono<Incidente> obtenerPorId(Long id) {
        return repository.findById(id);
    }
} 