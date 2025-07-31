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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private UsuarioService usuarioService;
    
    @Autowired
    private JwtUtil jwtUtil;

    // Endpoint de prueba para verificar conexión a BD
    @GetMapping("/test")
    public Mono<ResponseEntity<Map<String, Object>>> testConnection() {
        logger.info("Probando conexión a la base de datos");
        return usuarioService.findAll()
                .collectList()
                .map(usuarios -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("message", "Conexión exitosa");
                    response.put("totalUsuarios", usuarios.size());
                    response.put("status", "OK");
                    logger.info("Conexión exitosa. Total usuarios: {}", usuarios.size());
                    return ResponseEntity.ok(response);
                })
                .onErrorResume(e -> {
                    logger.error("Error en conexión a BD", e);
                    Map<String, Object> error = new HashMap<>();
                    error.put("message", "Error de conexión: " + e.getMessage());
                    error.put("status", "ERROR");
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error));
                });
    }

    @PostMapping("/login")
    public Mono<ResponseEntity<Map<String, Object>>> login(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String password = body.get("password");

        logger.info("Intento de login para email: {}", email);

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
                    logger.info("Usuario encontrado: {}", usuario.getEmail());
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
                        logger.info("Login exitoso para usuario: {}", usuario.getEmail());
                        return Mono.just(ResponseEntity.ok(response));
                    } else {
                        logger.warn("Contraseña incorrecta para usuario: {}", email);
                        Map<String, Object> error = new HashMap<>();
                        error.put("message", "Credenciales inválidas");
                        return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error));
                    }
                })
                .switchIfEmpty(Mono.defer(() -> {
                    logger.warn("Usuario no encontrado: {}", email);
                    Map<String, Object> error = new HashMap<>();
                    error.put("message", "Usuario no encontrado");
                    return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).body(error));
                }))
                .onErrorResume(e -> {
                    logger.error("Error en login para email: {}", email, e);
                    Map<String, Object> error = new HashMap<>();
                    error.put("message", "Error interno del servidor");
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error));
                });
    }

    @PostMapping("/register")
    public Mono<ResponseEntity<Map<String, Object>>> register(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String password = body.get("password");
        String nombre = body.get("nombre");
        String direccion = body.get("direccion");
        
        logger.info("Intento de registro para email: {}", email);
        
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
                logger.warn("Intento de registro con email ya existente: {}", email);
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
                    logger.info("Registro exitoso para usuario: {}", usuario.getEmail());
                    return ResponseEntity.ok(response);
                })
            )
            .onErrorResume(e -> {
                logger.error("Error en registro para email: {}", email, e);
                Map<String, Object> error = new HashMap<>();
                error.put("message", "Error interno del servidor");
                return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error));
            });
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
            logger.error("Error validando token", e);
            Map<String, Object> error = new HashMap<>();
            error.put("message", "Token inválido");
            return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error));
        }
    }
}