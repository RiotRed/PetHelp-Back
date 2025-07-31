package com.perros.registro_perros.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.perros.registro_perros.model.Distrito;
import com.perros.registro_perros.repository.DistritoRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/distritos")
@CrossOrigin(origins = "*")
public class DistritoController {
    
    @Autowired
    private DistritoRepository distritoRepository;

    // Público: Listar todos los distritos
    @GetMapping
    public Flux<Distrito> listarDistritos() {
        return distritoRepository.findAll();
    }

    // Público: Obtener distrito por ID
    @GetMapping("/{id}")
    public Mono<Distrito> obtenerDistrito(@PathVariable Long id) {
        return distritoRepository.findById(id);
    }

    // Registrador: Crear nuevo distrito
    @PostMapping
    public Mono<Distrito> crearDistrito(@RequestBody Distrito distrito, @RequestHeader("Authorization") String authHeader) {
        return distritoRepository.save(distrito);
    }

    // Registrador: Actualizar distrito
    @PutMapping("/{id}")
    public Mono<Distrito> actualizarDistrito(@PathVariable Long id, @RequestBody Distrito distrito, @RequestHeader("Authorization") String authHeader) {
        return distritoRepository.findById(id)
                .flatMap(existing -> {
                    distrito.setId(id);
                    return distritoRepository.save(distrito);
                });
    }

    // Registrador: Eliminar distrito
    @DeleteMapping("/{id}")
    public Mono<Void> eliminarDistrito(@PathVariable Long id, @RequestHeader("Authorization") String authHeader) {
        return distritoRepository.deleteById(id);
    }
} 