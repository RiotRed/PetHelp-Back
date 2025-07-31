package com.perros.registro_perros.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.perros.registro_perros.model.Usuario;
import com.perros.registro_perros.service.UsuarioService;
import com.perros.registro_perros.util.JwtUtil;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UsuarioService usuarioService;
    
    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public Mono<ResponseEntity<Map<String, Object>>> login(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String password = body.get("password");

        // Validaciones básicas
        if (email == null || email.trim().isEmpty()) {
            Map<String, Object> error = new HashMap<>();
            error.put("message", "El email es requerido");
            return Mono.just(ResponseEntity.badRequest().body(error));
        }

        if (password == null || password.trim().isEmpty()) {
            Map<String, Object> error = new HashMap<>();
            error.put("message", "La contraseña es requerida");
            return Mono.just(ResponseEntity.badRequest().body(error));
        }

        return usuarioService.findByEmail(email)
                .flatMap(usuario -> {
                    if (usuario.getPassword().equals(password)) {
                        Map<String, Object> response = new HashMap<>();
                        response.put("token", jwtUtil.generateToken(usuario.getId()));
                        
                        Map<String, Object> userInfo = new HashMap<>();
                        userInfo.put("id", usuario.getId());
                        userInfo.put("email", usuario.getEmail());
                        userInfo.put("nombre", usuario.getNombre());
                        userInfo.put("direccion", usuario.getDireccion());
                        userInfo.put("dueño", usuario.isDueño());
                        
                        response.put("user", userInfo);
                        return Mono.just(ResponseEntity.ok(response));
                    } else {
                        Map<String, Object> error = new HashMap<>();
                        error.put("message", "Credenciales inválidas");
                        return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error));
                    }
                })
                .switchIfEmpty(Mono.defer(() -> {
                    Map<String, Object> error = new HashMap<>();
                    error.put("message", "Usuario no encontrado");
                    return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).body(error));
                }));
    }

    @PostMapping("/register")
    public Mono<ResponseEntity<Map<String, Object>>> register(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String password = body.get("password");
        String nombre = body.get("nombre");
        String direccion = body.get("direccion");
        
        // Validaciones básicas
        if (email == null || email.trim().isEmpty()) {
            Map<String, Object> error = new HashMap<>();
            error.put("message", "El email es requerido");
            return Mono.just(ResponseEntity.badRequest().body(error));
        }

        if (password == null || password.trim().isEmpty()) {
            Map<String, Object> error = new HashMap<>();
            error.put("message", "La contraseña es requerida");
            return Mono.just(ResponseEntity.badRequest().body(error));
        }

        if (nombre == null || nombre.trim().isEmpty()) {
            Map<String, Object> error = new HashMap<>();
            error.put("message", "El nombre es requerido");
            return Mono.just(ResponseEntity.badRequest().body(error));
        }
        
        return usuarioService.findByEmail(email)
            .flatMap(u -> {
                Map<String, Object> error = new HashMap<>();
                error.put("message", "El correo ya está registrado");
                return Mono.just(ResponseEntity.badRequest().body(error));
            })
            .switchIfEmpty(
                usuarioService.save(new Usuario() {{
                    setEmail(email);
                    setPassword(password);
                    setNombre(nombre != null ? nombre : "");
                    setDireccion(direccion != null ? direccion : "");
                    setDueño(true);
                }})
                .map(usuario -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("token", jwtUtil.generateToken(usuario.getId()));
                    
                    Map<String, Object> userInfo = new HashMap<>();
                    userInfo.put("id", usuario.getId());
                    userInfo.put("email", usuario.getEmail());
                    userInfo.put("nombre", usuario.getNombre());
                    userInfo.put("direccion", usuario.getDireccion());
                    userInfo.put("dueño", usuario.isDueño());
                    
                    response.put("user", userInfo);
                    return ResponseEntity.ok(response);
                })
            );
    }

    @GetMapping("/validate")
    public Mono<ResponseEntity<Map<String, Object>>> validateToken(@RequestHeader("Authorization") String authHeader) {
        try {
            String token = jwtUtil.extractTokenFromHeader(authHeader);
            Long userId = jwtUtil.extractUserId(token);
            
            return usuarioService.findById(userId)
                    .map(usuario -> {
                        Map<String, Object> userInfo = new HashMap<>();
                        userInfo.put("id", usuario.getId());
                        userInfo.put("email", usuario.getEmail());
                        userInfo.put("nombre", usuario.getNombre());
                        userInfo.put("direccion", usuario.getDireccion());
                        userInfo.put("dueño", usuario.isDueño());
                        return ResponseEntity.ok(userInfo);
                    })
                    .switchIfEmpty(Mono.defer(() -> {
                        Map<String, Object> error = new HashMap<>();
                        error.put("message", "Usuario no encontrado");
                        return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).body(error));
                    }));
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("message", "Token inválido");
            return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error));
        }
    }
}