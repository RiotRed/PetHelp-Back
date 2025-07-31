package com.perros.registro_perros.service;

import com.perros.registro_perros.model.Raza;
import com.perros.registro_perros.repository.RazaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class RazaService {

    private final RazaRepository repo;

    public Flux<Raza> listar() {
        return repo.findAll();
    }

    public Mono<Raza> registrar(Raza r) {
        return repo.save(r);
    }

    public Mono<Void> eliminar(Long id) {
        return repo.deleteById(id);
    }

    public Mono<Raza> obtenerPorId(Long id) {
        return repo.findById(id);
    }

    public Mono<Raza> actualizar(Long id, Raza nuevoRaza) {
        return repo.findById(id)
            .flatMap(raza -> {
                raza.setNombre(nuevoRaza.getNombre());
                // agrega aquí otros setters según tu modelo
                return repo.save(raza);
            });
    }
}
