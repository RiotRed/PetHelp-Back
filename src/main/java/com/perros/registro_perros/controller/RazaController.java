package com.perros.registro_perros.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.perros.registro_perros.model.Raza;
import com.perros.registro_perros.repository.RazaRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/razas")
@CrossOrigin(origins = "*")
public class RazaController {
    
    @Autowired
    private RazaRepository razaRepository;

    // Público: Listar todas las razas
    @GetMapping
    public Flux<Raza> listarRazas() {
        return razaRepository.findAll();
    }

    // Público: Obtener raza por ID
    @GetMapping("/{id}")
    public Mono<Raza> obtenerRaza(@PathVariable Long id) {
        return razaRepository.findById(id);
    }

    // Registrador: Crear nueva raza
    @PostMapping
    public Mono<Raza> crearRaza(@RequestBody Raza raza, @RequestHeader("Authorization") String authHeader) {
        return razaRepository.save(raza);
    }

    // Registrador: Actualizar raza
    @PutMapping("/{id}")
    public Mono<Raza> actualizarRaza(@PathVariable Long id, @RequestBody Raza raza, @RequestHeader("Authorization") String authHeader) {
        return razaRepository.findById(id)
                .flatMap(existing -> {
                    raza.setId(id);
                    return razaRepository.save(raza);
                });
    }

    // Registrador: Eliminar raza
    @DeleteMapping("/{id}")
    public Mono<Void> eliminarRaza(@PathVariable Long id, @RequestHeader("Authorization") String authHeader) {
        return razaRepository.deleteById(id);
    }
} 