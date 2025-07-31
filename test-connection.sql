-- Script para verificar la conexión a la base de datos
USE pethelpbd;

-- Verificar que las tablas existen
SHOW TABLES;

-- Verificar datos en usuarios
SELECT COUNT(*) as total_usuarios FROM usuarios;

-- Verificar datos en perros
SELECT COUNT(*) as total_perros FROM perros;

-- Verificar datos en distritos
SELECT COUNT(*) as total_distritos FROM distritos;

-- Verificar datos en razas
SELECT COUNT(*) as total_razas FROM razas;

-- Verificar datos en incidentes
SELECT COUNT(*) as total_incidentes FROM incidente; 