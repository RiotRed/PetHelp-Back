package com.perros.registro_perros.DTO;

import lombok.Data;

@Data
public class PerroConUsuarioDTO {
    private Long id;
    private String nombre;
    private Long distritoid;
    private Long razaId;
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
