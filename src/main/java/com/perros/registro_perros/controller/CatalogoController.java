package com.perros.registro_perros.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.perros.registro_perros.model.Distrito;
import com.perros.registro_perros.model.Raza;
import com.perros.registro_perros.repository.DistritoRepository;
import com.perros.registro_perros.repository.RazaRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/catalogo")
@CrossOrigin(origins = "*")
public class CatalogoController {
    
    @Autowired
    private RazaRepository razaRepository;
    
    @Autowired
    private DistritoRepository distritoRepository;

    // Público: Listar todas las razas
    @GetMapping("/razas")
    public Flux<Raza> listarRazas() {
        return razaRepository.findAll();
    }

    // Registrador: Crear nueva raza
    @PostMapping("/razas")
    public Mono<Raza> crearRaza(@RequestBody Raza raza, @RequestHeader("Authorization") String authHeader) {
        return razaRepository.save(raza);
    }

    // Público: Obtener raza por ID
    @GetMapping("/razas/{id}")
    public Mono<Raza> obtenerRaza(@PathVariable Long id) {
        return razaRepository.findById(id);
    }

    // Registrador: Actualizar raza
    @PutMapping("/razas/{id}")
    public Mono<Raza> actualizarRaza(@PathVariable Long id, @RequestBody Raza raza, @RequestHeader("Authorization") String authHeader) {
        return razaRepository.findById(id)
                .flatMap(existing -> {
                    raza.setId(id);
                    return razaRepository.save(raza);
                });
    }

    // Registrador: Eliminar raza
    @DeleteMapping("/razas/{id}")
    public Mono<Void> eliminarRaza(@PathVariable Long id, @RequestHeader("Authorization") String authHeader) {
        return razaRepository.deleteById(id);
    }

    // Público: Listar todos los distritos
    @GetMapping("/distritos")
    public Flux<Distrito> listarDistritos() {
        return distritoRepository.findAll();
    }

    // Registrador: Crear nuevo distrito
    @PostMapping("/distritos")
    public Mono<Distrito> crearDistrito(@RequestBody Distrito distrito, @RequestHeader("Authorization") String authHeader) {
        return distritoRepository.save(distrito);
    }

    // Público: Obtener distrito por ID
    @GetMapping("/distritos/{id}")
    public Mono<Distrito> obtenerDistrito(@PathVariable Long id) {
        return distritoRepository.findById(id);
    }

    // Registrador: Actualizar distrito
    @PutMapping("/distritos/{id}")
    public Mono<Distrito> actualizarDistrito(@PathVariable Long id, @RequestBody Distrito distrito, @RequestHeader("Authorization") String authHeader) {
        return distritoRepository.findById(id)
                .flatMap(existing -> {
                    distrito.setId(id);
                    return distritoRepository.save(distrito);
                });
    }

    // Registrador: Eliminar distrito
    @DeleteMapping("/distritos/{id}")
    public Mono<Void> eliminarDistrito(@PathVariable Long id, @RequestHeader("Authorization") String authHeader) {
        return distritoRepository.deleteById(id);
    }
} 