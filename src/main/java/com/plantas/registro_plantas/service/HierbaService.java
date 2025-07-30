package com.plantas.registro_plantas.service;

import org.springframework.stereotype.Service;

import com.plantas.registro_plantas.model.Hierba;
import com.plantas.registro_plantas.repository.HierbaRepository;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class HierbaService {

    private final HierbaRepository repo;

    public Flux<Hierba> listar() {
        return repo.findAll();
    }

    public Mono<Hierba> registrar(Hierba h) {
        return repo.save(h);
    }

    public Mono<Void> eliminar(Long id) {
        return repo.deleteById(id);
    }

    public Mono<Hierba> obtenerPorId(Long id) {
        return repo.findById(id);
    }

    public Flux<Hierba> listarPorUsuario(Long usuarioId) {
        return repo.findAllByUsuarioId(usuarioId);
    }

    public Mono<Hierba> actualizar(Long id, Long usuarioId, Hierba nuevaHierba) {
        return repo.findByIdAndUsuarioId(id, usuarioId)
            .flatMap(hierba -> {
                hierba.setNombre(nuevaHierba.getNombre());
                hierba.setUso(nuevaHierba.getUso());
                hierba.setOrigen(nuevaHierba.getOrigen());
                hierba.setPropiedades(nuevaHierba.getPropiedades());
                return repo.save(hierba);
            });
    }

    public Mono<Void> eliminarPorUsuario(Long id, Long usuarioId) {
        return repo.findByIdAndUsuarioId(id, usuarioId)
            .flatMap(hierba -> repo.deleteById(hierba.getId()));
    }
}
