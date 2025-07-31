package com.perros.registro_perros.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.perros.registro_perros.model.Perro;
import com.perros.registro_perros.model.Incidente;
import com.perros.registro_perros.service.PerroService;
import com.perros.registro_perros.service.IncidenteService;

import reactor.core.publisher.Mono;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.Objects;

@RestController
@RequestMapping("/api/estadisticas")
@CrossOrigin(origins = "*")
public class EstadisticasController {

    @Autowired
    private PerroService perroService;

    @Autowired
    private IncidenteService incidenteService;

    // Público: Obtener densidad canina por distrito
    @GetMapping("/densidad-por-distrito")
    public Mono<Map<String, Object>> obtenerDensidadPorDistrito() {
        return perroService.listar()
                .collectList()
                .map(perros -> {
                    Map<String, Object> resultado = new HashMap<>();
                    Map<Long, Long> densidad = perros.stream()
                            .collect(Collectors.groupingBy(
                                    Perro::getDistritoid,
                                    Collectors.counting()
                            ));
                    resultado.put("densidadPorDistrito", densidad);
                    return resultado;
                });
    }

    // Público: Obtener estadísticas de perros por tamaño
    @GetMapping("/perros-por-tamano")
    public Mono<Map<String, Object>> obtenerPerrosPorTamano() {
        return perroService.listar()
                .collectList()
                .map(perros -> {
                    Map<String, Object> resultado = new HashMap<>();
                    Map<String, Long> porTamano = perros.stream()
                            .collect(Collectors.groupingBy(
                                    Perro::getTamanio,
                                    Collectors.counting()
                            ));
                    resultado.put("perrosPorTamano", porTamano);
                    return resultado;
                });
    }

    // Público: Obtener estadísticas de perros por comportamiento
    @GetMapping("/perros-por-comportamiento")
    public Mono<Map<String, Object>> obtenerPerrosPorComportamiento() {
        return perroService.listar()
                .collectList()
                .map(perros -> {
                    Map<String, Object> resultado = new HashMap<>();
                    Map<String, Long> porComportamiento = perros.stream()
                            .collect(Collectors.groupingBy(
                                    Perro::getComportamiento,
                                    Collectors.counting()
                            ));
                    resultado.put("perrosPorComportamiento", porComportamiento);
                    return resultado;
                });
    }

    // Público: Obtener zonas con alta densidad canina
    @GetMapping("/zonas-alta-densidad")
    public Mono<Map<String, Object>> obtenerZonasAltaDensidad() {
        return perroService.listar()
                .collectList()
                .map(perros -> {
                    Map<String, Object> resultado = new HashMap<>();
                    Map<Long, Long> densidad = perros.stream()
                            .collect(Collectors.groupingBy(
                                    Perro::getDistritoid,
                                    Collectors.counting()
                            ));

                    // Filtrar distritos con alta densidad (más de 10 perros)
                    Map<Long, Long> altaDensidad = densidad.entrySet().stream()
                            .filter(entry -> entry.getValue() > 10)
                            .collect(Collectors.toMap(
                                    Map.Entry::getKey,
                                    Map.Entry::getValue
                            ));

                    resultado.put("zonasAltaDensidad", altaDensidad);
                    return resultado;
                });
    }

    // Registrador: Obtener razas con mayor frecuencia de incidentes
    @GetMapping("/razas-con-incidentes")
    public Mono<Map<String, Object>> obtenerRazasConIncidentes() {
        return incidenteService.listarTodos()
                .collectList()
                .flatMap(incidentes -> perroService.listar()
                        .collectList()
                        .map(perros -> {
                            Map<String, Object> resultado = new HashMap<>();
                            Map<Long, Long> incidentesPorRaza = incidentes.stream()
                                    .map(incidente -> perros.stream()
                                            .filter(p -> Objects.equals(p.getId(), incidente.getPerroId()))
                                            .findFirst()
                                            .map(Perro::getRazaid)
                                            .orElse(null))
                                    .filter(Objects::nonNull)
                                    .collect(Collectors.groupingBy(
                                            razaId -> razaId,
                                            Collectors.counting()
                                    ));
                            resultado.put("razasConIncidentes", incidentesPorRaza);
                            return resultado;
                        }));
    }

    // Registrador: Obtener estadísticas de incidentes por tipo
    @GetMapping("/incidentes-por-tipo")
    public Mono<Map<String, Object>> obtenerIncidentesPorTipo() {
        return incidenteService.listarTodos()
                .collectList()
                .map(incidentes -> {
                    Map<String, Object> resultado = new HashMap<>();
                    Map<String, Long> porTipo = incidentes.stream()
                            .collect(Collectors.groupingBy(
                                    Incidente::getTipo,
                                    Collectors.counting()
                            ));
                    resultado.put("incidentesPorTipo", porTipo);
                    return resultado;
                });
    }
}