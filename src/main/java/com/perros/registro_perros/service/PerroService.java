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
        // Mantener el usuarioId tal como viene, incluso si es null
        return repo.save(perro);
    }

    public Flux<PerroConUsuarioDTO> findPerrosConUsuario() {
        return repo.findAll()
            .doOnNext(perro -> {
                System.out.println("DEBUG - Perro ID: " + perro.getId() + 
                                 ", Nombre: " + perro.getNombre() + 
                                 ", UsuarioId: " + perro.getUsuarioId());
            })
            .flatMap(perro -> {
                // Always set the usuarioId in the DTO, even if it's null
                PerroConUsuarioDTO dto = mapToDto(perro, null);
                
                // If perro has a usuarioId, try to fetch the user details
                if (perro.getUsuarioId() != null) {
                    return usuarioRepository.findById(perro.getUsuarioId())
                            .map(usuario -> mapToDto(perro, usuario))
                            .defaultIfEmpty(dto);
                } else {
                    return Mono.just(dto);
                }
            });
    }

    private PerroConUsuarioDTO mapToDto(Perro perro, Usuario usuario) {
        PerroConUsuarioDTO dto = new PerroConUsuarioDTO();
        
        // Manejo seguro de valores null
        dto.setId(perro.getId());
        dto.setNombre(perro.getNombre() != null ? perro.getNombre() : "");
        dto.setDistritoid(perro.getDistritoid());
        dto.setRazaId(perro.getRazaid());
        dto.setTamanio(perro.getTamanio() != null ? perro.getTamanio() : "");
        dto.setComportamiento(perro.getComportamiento() != null ? perro.getComportamiento() : "");
        dto.setColor(perro.getColor() != null ? perro.getColor() : "");
        dto.setGenero(perro.getGenero() != null ? perro.getGenero() : "");
        dto.setEdad(perro.getEdad());
        dto.setVacunado(perro.getVacunado() != null ? perro.getVacunado() : false);
        dto.setEsterilizado(perro.getEsterilizado() != null ? perro.getEsterilizado() : false);
        // Mantener el valor real de la BD
        dto.setUsuarioId(perro.getUsuarioId());
        dto.setDireccion(perro.getDireccion() != null ? perro.getDireccion() : "");

        return dto;
    }
}

