package com.perros.registro_perros.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.server.WebFilter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Configuration
public class CorsGlobalConfiguration {

    @Bean
    public WebFluxConfigurer corsConfigurer() {
        return new WebFluxConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins(
                            "http://localhost:3000",
                            "http://localhost:5173",
                            "http://localhost:4173"
                        )
                        .allowedMethods("*")
                        .allowedHeaders("*")
                        .allowCredentials(true);
            }
        };
    }

    // Filtro CORS global para controladores y routers funcionales
    @Bean
    public WebFilter corsFilter() {
        return (ServerWebExchange ctx, org.springframework.web.server.WebFilterChain chain) -> {
            ServerHttpRequest request = ctx.getRequest();
            String origin = request.getHeaders().getOrigin();
            if (origin != null) {
                ctx.getResponse().getHeaders().add("Access-Control-Allow-Origin", origin);
                ctx.getResponse().getHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
                ctx.getResponse().getHeaders().add("Access-Control-Allow-Headers", "Origin, Content-Type, Accept, Authorization");
                ctx.getResponse().getHeaders().add("Access-Control-Allow-Credentials", "true");
            }
            if (request.getMethod() == HttpMethod.OPTIONS) {
                ctx.getResponse().setStatusCode(org.springframework.http.HttpStatus.OK);
                return Mono.empty();
            }
            return chain.filter(ctx);
        };
    }
}
