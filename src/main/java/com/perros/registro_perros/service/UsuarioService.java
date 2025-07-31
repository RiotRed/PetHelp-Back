package com.perros.registro_perros.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.perros.registro_perros.model.Usuario;
import com.perros.registro_perros.repository.UsuarioRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Flux<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    public Mono<Usuario> findById(Long id) {
        return usuarioRepository.findById(id);
    }

    public Mono<Usuario> save(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    public Mono<Usuario> update(Long id, Usuario usuario) {
        return usuarioRepository.findById(id)
                .flatMap(existing -> {
                    usuario.setId(id);
                    return usuarioRepository.save(usuario);
                });
    }

    public Mono<Void> delete(Long id) {
        return usuarioRepository.deleteById(id);
    }

    public Mono<Usuario> findByEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }
}