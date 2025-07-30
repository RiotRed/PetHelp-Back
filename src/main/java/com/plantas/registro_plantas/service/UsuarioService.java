package com.plantas.registro_plantas.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.plantas.registro_plantas.model.Usuario;
import com.plantas.registro_plantas.repository.UsuarioRepository;

import reactor.core.publisher.Mono;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Mono<Usuario> findByEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    public Mono<Usuario> save(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }
}
