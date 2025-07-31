-- Script para verificar la base de datos pethelp
USE pethelp;

-- Verificar que las tablas existen
SHOW TABLES;

-- Verificar datos en usuarios (para login)
SELECT id, email, nombre, password FROM usuarios LIMIT 5;

-- Verificar datos en perros (para estadísticas)
SELECT id, nombre, tamanio, comportamiento, genero, color, vacunado, esterilizado FROM perros LIMIT 10;

-- Verificar datos en distritos
SELECT * FROM distritos;

-- Verificar datos en razas
SELECT * FROM razas;

-- Verificar datos en incidentes
SELECT * FROM incidente LIMIT 5;

-- Probar login con usuario existente
-- Usuario: ana@gmail.com, Password: pass123
SELECT * FROM usuarios WHERE email = 'ana@gmail.com';

-- Contar total de perros
SELECT COUNT(*) as total_perros FROM perros;

-- Estadísticas por tamaño
SELECT tamanio, COUNT(*) as cantidad 
FROM perros 
WHERE tamanio IS NOT NULL 
GROUP BY tamanio;

-- Estadísticas por comportamiento
SELECT comportamiento, COUNT(*) as cantidad 
FROM perros 
WHERE comportamiento IS NOT NULL 
GROUP BY comportamiento;

-- Estadísticas por género
SELECT genero, COUNT(*) as cantidad 
FROM perros 
WHERE genero IS NOT NULL 
GROUP BY genero;

-- Estadísticas por color
SELECT color, COUNT(*) as cantidad 
FROM perros 
WHERE color IS NOT NULL 
GROUP BY color;

-- Estadísticas por vacunación
SELECT 
    CASE 
        WHEN vacunado = 1 THEN 'Vacunado'
        WHEN vacunado = 0 THEN 'No vacunado'
        ELSE 'Sin especificar'
    END as estado_vacunacion,
    COUNT(*) as cantidad
FROM perros 
GROUP BY vacunado;

-- Estadísticas por esterilización
SELECT 
    CASE 
        WHEN esterilizado = 1 THEN 'Esterilizado'
        WHEN esterilizado = 0 THEN 'No esterilizado'
        ELSE 'Sin especificar'
    END as estado_esterilizacion,
    COUNT(*) as cantidad
FROM perros 
GROUP BY esterilizado; 