package com.perros.registro_perros.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.mapping.Column;

import lombok.Data;
import java.time.LocalDateTime;

@Data
@Table("incidente")
public class Incidente {
    @Id
    private Long id;
    
    @Column("perroid")
    private Long perroId;
    
    @Column("tipo")
    private String tipo; // MORDIDA, AGRESION, PERDIDA, OTRO
    
    @Column("descripcion")
    private String descripcion;
    
    @Column("ubicacion")
    private String ubicacion;
    
    @Column("fechaincidente")
    private LocalDateTime fechaIncidente;
    
    @Column("reportadopor")
    private String reportadoPor;
    
    @Column("telefonoreporte")
    private String telefonoReporte;
    
    @Column("estado")
    private String estado; // REPORTADO, EN_INVESTIGACION, RESUELTO
    
    @Column("fechareporte")
    private LocalDateTime fechaReporte;
    
    @Column("usuarioid")
    private Long usuarioId;
} 