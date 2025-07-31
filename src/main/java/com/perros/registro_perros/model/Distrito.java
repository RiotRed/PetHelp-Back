package com.perros.registro_perros.model;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Data;

@Data
@Table("distritos")
public class Distrito {
    @Id
    private Long id;
    private String nombre;
}
