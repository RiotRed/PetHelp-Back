package com.perros.registro_perros.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
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
    public Mono<Map<String, Object>> login(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String password = body.get("password");

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
                        return Mono.just(response);
                    } else {
                        return Mono.error(new RuntimeException("Credenciales inválidas"));
                    }
                })
                .switchIfEmpty(Mono.error(new RuntimeException("Usuario no encontrado")));
    }

    @PostMapping("/register")
    public Mono<Map<String, Object>> register(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String password = body.get("password");
        String nombre = body.get("nombre");
        String direccion = body.get("direccion");
        
        return usuarioService.findByEmail(email)
            .flatMap(u -> Mono.<Map<String, Object>>error(new RuntimeException("El correo ya está registrado")))
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
                    return response;
                })
            );
    }

    @GetMapping("/validate")
    public Mono<Map<String, Object>> validateToken(@RequestHeader("Authorization") String authHeader) {
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
                    return userInfo;
                })
                .switchIfEmpty(Mono.error(new RuntimeException("Usuario no encontrado")));
    }
}