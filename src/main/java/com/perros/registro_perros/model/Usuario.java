package com.perros.registro_perros.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.mapping.Column;

@Table("usuarios")
public class Usuario {

    @Id
    private Long id;

    @Column("email")
    private String email;

    @Column("nombre")
    private String nombre;

    @Column("direccion")
    private String direccion;
    
    @Column("password")
    private String password;

    @Column("dueño")
    private boolean dueño;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getDireccion() {
        return direccion;
    }
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
        public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public boolean isDueño() {
        return dueño;
    }
}