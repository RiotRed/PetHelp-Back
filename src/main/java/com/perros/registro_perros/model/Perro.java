package com.perros.registro_perros.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Data;

@Data
@Table("perros")
public class Perro {
    @Id
    private Long id;
    private String nombre;
    private String dueño;
    private String emailDueño;
    private int distritoid;
    private int razaid;
    private String tamaño; // PEQUEÑO, MEDIANO, GRANDE
    private String comportamiento; // CALMO, AGRESIVO, JUGUETON, TEMEROSO
    private String color;
    private String genero; // MACHO, HEMBRA
    private Integer edad;
    private Boolean vacunado;
    private Boolean esterilizado;
    private Long usuarioId;
    private String direccion;
}
