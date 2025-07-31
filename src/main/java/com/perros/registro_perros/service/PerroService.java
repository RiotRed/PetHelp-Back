package com.perros.registro_perros.service;

import org.springframework.stereotype.Service;

import com.perros.registro_perros.DTO.PerroConUsuarioDTO;
import com.perros.registro_perros.model.Perro;
import com.perros.registro_perros.model.Usuario;
import com.perros.registro_perros.repository.PerroRepository;
import com.perros.registro_perros.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class PerroService {

    private final PerroRepository repo;
    private final UsuarioRepository usuarioRepository;

    public Flux<Perro> listar() {
        return repo.findAll();
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
                perro.setDistritoid(nuevoPerro.getDistritoid());
                perro.setRazaid(nuevoPerro.getRazaid());
                perro.setTamanio(nuevoPerro.getTamanio());
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
            .flatMap(perro -> repo.deleteById(perro.getId()));
    }

    public Mono<Perro> registrar(Perro perro) {
        return repo.save(perro);
    }

    public Flux<PerroConUsuarioDTO> findPerrosConUsuario() {
        return repo.findAll()
            .flatMap(perro -> {
                Mono<Usuario> usuarioMono = perro.getUsuarioId() != null
                        ? usuarioRepository.findById(perro.getUsuarioId())
                        : Mono.empty();

                return usuarioMono
                        .map(usuario -> mapToDto(perro, usuario))
                        .defaultIfEmpty(mapToDto(perro, null));
            });
    }

    private PerroConUsuarioDTO mapToDto(Perro perro, Usuario usuario) {
    PerroConUsuarioDTO dto = new PerroConUsuarioDTO();
    dto.setId(perro.getId());
    dto.setNombre(perro.getNombre());
    dto.setDistritoid(perro.getDistritoid());
    dto.setRazaId(perro.getRazaid());
    dto.setTamaño(perro.getTamanio());         // 👈 Aquí
    dto.setComportamiento(perro.getComportamiento());
    dto.setColor(perro.getColor());
    dto.setGenero(perro.getGenero());
    dto.setEdad(perro.getEdad());
    dto.setVacunado(perro.getVacunado());
    dto.setEsterilizado(perro.getEsterilizado());
    dto.setUsuarioId(perro.getUsuarioId());   // 👈 Aquí también
    dto.setDireccion(perro.getDireccion());

    if (usuario != null) {
        dto.setDueño(usuario.getNombre());
        dto.setEmailDueño(usuario.getEmail());
    }

    return dto;
    }
}

