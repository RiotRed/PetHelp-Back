package com.perros.registro_perros.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.mapping.Column;

@Data
@Table("perros")
public class Perro {
    @Id
    private Long id;
    
    @Column("nombre")
    private String nombre;
    
    @Column("distritoid")
    private Long distritoid;
    
    @Column("razaid")
    private Long razaid;
    
    @Column("tamanio")
    private String tamanio;
    
    @Column("comportamiento")
    private String comportamiento;
    
    @Column("color")
    private String color;
    
    @Column("genero")
    private String genero;
    
    @Column("edad")
    private Integer edad;
    
    @Column("vacunado")
    private Boolean vacunado;
    
    @Column("esterilizado")
    private Boolean esterilizado;
    
    @Column("usuarioid")
    private Long usuarioId;
    
    @Column("direccion")
    private String direccion;
}
