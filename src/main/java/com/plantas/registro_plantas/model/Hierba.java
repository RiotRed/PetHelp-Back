package com.plantas.registro_plantas.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Data;

@Data
@Table("hierbas")
public class Hierba {
    @Id
    private Long id;
    private String nombre;
    private String uso;
    private String origen;
    private String propiedades;
    private Long usuarioId;
}
