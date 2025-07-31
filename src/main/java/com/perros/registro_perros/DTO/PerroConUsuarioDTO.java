package com.perros.registro_perros.DTO;

import lombok.Data;

@Data
public class PerroConUsuarioDTO {
    private Long id;
    private String nombre;
    private String dueño;
    private String emailDueño;
    private Long distritoid;
    private Long razaId;
    private String tamaño;
    private String comportamiento;
    private String color;
    private String genero;
    private Integer edad;
    private Boolean vacunado;
    private Boolean esterilizado;
    private Long usuarioId;
    private String direccion;
}
