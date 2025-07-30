package com.perros.registro_perros.service;

import org.springframework.stereotype.Service;

import com.perros.registro_perros.model.Perro;
import com.perros.registro_perros.repository.PerroRepository;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class PerroService {

    private final PerroRepository repo;

    public Flux<Perro> listar() {
        return repo.findAll();
    }

    public Mono<Perro> registrar(Perro h) {
        return repo.save(h);
    }

    public Mono<Void> eliminar(Long id) {
        return repo.deleteById(id);
    }

    public Mono<Perro> obtenerPorId(Long id) {
        return repo.findById(id);
    }

    public Flux<Perro> listarPorUsuario(Long usuarioId) {
        return repo.findAllByUsuarioId(usuarioId);
    }

    public Mono<Perro> actualizar(Long id, Long usuarioId, Perro nuevoPerro) {
        return repo.findByIdAndUsuarioId(id, usuarioId)
            .flatMap(perro -> {
                perro.setNombre(nuevoPerro.getNombre());
                perro.setDueño(nuevoPerro.getDueño());
                perro.setEmailDueño(nuevoPerro.getEmailDueño());
                perro.setDistritoid(nuevoPerro.getDistritoid());
                perro.setRazaid(nuevoPerro.getRazaid());
                perro.setTamaño(nuevoPerro.getTamaño());
                perro.setComportamiento(nuevoPerro.getComportamiento());
                perro.setColor(nuevoPerro.getColor());
                perro.setGenero(nuevoPerro.getGenero());
                perro.setEdad(nuevoPerro.getEdad());
                perro.setVacunado(nuevoPerro.getVacunado());
                perro.setEsterilizado(nuevoPerro.getEsterilizado());
                perro.setDireccion(nuevoPerro.getDireccion());
                return repo.save(perro);
            });
    }

    public Mono<Void> eliminarPorUsuario(Long id, Long usuarioId) {
        return repo.findByIdAndUsuarioId(id, usuarioId)
            .flatMap(hierba -> repo.deleteById(hierba.getId()));
    }
}
