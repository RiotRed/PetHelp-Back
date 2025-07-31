package com.perros.registro_perros.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Data;
import java.time.LocalDateTime;

@Data
@Table("incidente")
public class Incidente {
    @Id
    private Long id;
    private Long perroId;
    private String tipo; // MORDIDA, AGRESION, PERDIDA, OTRO
    private String descripcion;
    private String ubicacion;
    private LocalDateTime fechaIncidente;
    private String reportadoPor;
    private String telefonoReporte;
    private String estado; // REPORTADO, EN_INVESTIGACION, RESUELTO
    private LocalDateTime fechaReporte;
    private Long usuarioId;
} 