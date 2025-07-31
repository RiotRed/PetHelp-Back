package com.perros.registro_perros.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("perros")
public class Perro {
    @Id
    private Long id;
    private String nombre;
    private Long distritoid;
    private Long razaid;
    private String tamanio;
    private String comportamiento;
    private String color;
    private String genero;
    private Integer edad;
    private Boolean vacunado;
    private Boolean esterilizado;
    private Long usuarioId;
    private String direccion;
}
