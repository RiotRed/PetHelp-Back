package com.perros.registro_perros.service;

import com.perros.registro_perros.model.Distrito;
import com.perros.registro_perros.repository.DistritoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class DistritoService {

    private final DistritoRepository repo;

    public Flux<Distrito> listar() {
        return repo.findAll();
    }

    public Mono<Distrito> registrar(Distrito d) {
        return repo.save(d);
    }

    public Mono<Void> eliminar(Long id) {
        return repo.deleteById(id);
    }

    public Mono<Distrito> obtenerPorId(Long id) {
        return repo.findById(id);
    }

    public Mono<Distrito> actualizar(Long id, Distrito nuevoDistrito) {
        return repo.findById(id)
            .flatMap(distrito -> {
                distrito.setNombre(nuevoDistrito.getNombre());
                // agrega aquí otros setters según tu modelo
                return repo.save(distrito);
            });
    }
}
