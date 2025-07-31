package com.perros.registro_perros.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.perros.registro_perros.model.Perro;
import com.perros.registro_perros.service.PerroService;
import com.perros.registro_perros.util.JwtUtil;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/perros")
@CrossOrigin(origins = "*")
public class PerroController {
    
    @Autowired
    private PerroService service;
    
    @Autowired
    private JwtUtil jwtUtil;

    // Público: Obtener todos los perros
    @GetMapping
    public Flux<Perro> listarTodos() {
        return service.listar();
    }

    // Público: Obtener un perro por ID
    @GetMapping("/{id}")
    public Mono<Perro> obtenerPorId(@PathVariable Long id) {
        return service.obtenerPorId(id);
    }

    // Registrador: Registrar perros y sus dueños
    @PostMapping
    public Mono<Perro> registrar(@RequestBody Perro perro, @RequestHeader("Authorization") String authHeader) {
        String token = jwtUtil.extractTokenFromHeader(authHeader);
        Long usuarioId = jwtUtil.extractUserId(token);
        perro.setUsuarioId(usuarioId);
        return service.registrar(perro);
    }

    // Registrador: Actualizar perro
    @PutMapping("/{id}")
    public Mono<Perro> actualizar(@PathVariable Long id, @RequestBody Perro perro, @RequestHeader("Authorization") String authHeader) {
        String token = jwtUtil.extractTokenFromHeader(authHeader);
        Long usuarioId = jwtUtil.extractUserId(token);
        return service.actualizar(id, usuarioId, perro);
    }

    // Registrador: Eliminar perro
    @DeleteMapping("/{id}")
    public Mono<Void> eliminar(@PathVariable Long id, @RequestHeader("Authorization") String authHeader) {
        String token = jwtUtil.extractTokenFromHeader(authHeader);
        Long usuarioId = jwtUtil.extractUserId(token);
        return service.eliminarPorUsuario(id, usuarioId);
    }

    // Público: Buscar perros por dueño (usando usuarioId)
    @GetMapping("/buscar-dueno")
    public Flux<Perro> buscarPorDueno(@RequestParam String nombre) {
        // Por ahora, retornamos todos los perros ya que necesitamos implementar
        // la búsqueda por nombre de dueño a través de la relación con Usuario
        return service.listar();
    }

    // Público: Buscar perros por raza
    @GetMapping("/raza")
    public Flux<Perro> buscarPorRaza(@RequestParam String raza) {
        return service.listar()
                .filter(perro -> perro.getRazaid() != null && 
                       perro.getRazaid().toString().equals(raza));
    }

    // Público: Buscar perros por tamaño
    @GetMapping("/tamaño")
    public Flux<Perro> buscarPorTamaño(@RequestParam String tamaño) {
        return service.listar()
                .filter(perro -> perro.getTamanio() != null && 
                       perro.getTamanio().equalsIgnoreCase(tamaño));
    }

    // Público: Buscar perros por comportamiento
    @GetMapping("/comportamiento")
    public Flux<Perro> buscarPorComportamiento(@RequestParam String comportamiento) {
        return service.listar()
                .filter(perro -> perro.getComportamiento() != null && 
                       perro.getComportamiento().equalsIgnoreCase(comportamiento));
    }

    // Público: Buscar perros por ubicación
    @GetMapping("/ubicacion")
    public Flux<Perro> buscarPorUbicacion(@RequestParam String ubicacion) {
        return service.listar()
                .filter(perro -> perro.getDireccion() != null && 
                       perro.getDireccion().toLowerCase().contains(ubicacion.toLowerCase()));
    }

    // Público: Obtener mapa de densidad canina
    @GetMapping("/densidad")
    public Mono<Map<String, Object>> obtenerMapaDensidad() {
        return service.listar()
                .collectList()
                .map(perros -> {
                    Map<String, Object> resultado = new HashMap<>();
                    Map<Long, Long> densidad = perros.stream()
                            .collect(java.util.stream.Collectors.groupingBy(
                                    Perro::getDistritoid,
                                    java.util.stream.Collectors.counting()
                            ));
                    resultado.put("densidadPorDistrito", densidad);
                    return resultado;
                });
    }

    // Público: Obtener estadísticas generales
    @GetMapping("/estadisticas")
    public Mono<Map<String, Object>> obtenerEstadisticasGenerales() {
        return service.listar()
                .collectList()
                .map(perros -> {
                    Map<String, Object> resultado = new HashMap<>();
                    resultado.put("totalPerros", perros.size());
                    
                    Map<String, Long> porTamaño = perros.stream()
                            .collect(java.util.stream.Collectors.groupingBy(
                                    Perro::getTamanio,
                                    java.util.stream.Collectors.counting()
                            ));
                    resultado.put("porTamaño", porTamaño);
                    
                    Map<String, Long> porComportamiento = perros.stream()
                            .collect(java.util.stream.Collectors.groupingBy(
                                    Perro::getComportamiento,
                                    java.util.stream.Collectors.counting()
                            ));
                    resultado.put("porComportamiento", porComportamiento);
                    
                    return resultado;
                });
    }


} 