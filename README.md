# Sistema de Registro de Perros - API

## Descripción
Sistema web interactivo para el registro y gestión de perros y sus dueños, con funcionalidades para clasificación, estadísticas y visualización de densidad canina.

## Funcionalidades Principales

### Para Registradores:
- ✅ Registrar perros y sus dueños
- ✅ Clasificar perros por raza, tamaño, comportamiento y ubicación
- ✅ Gestionar catálogos de razas y distritos
- ✅ Reportar incidentes relacionados con perros
- ✅ Identificar razas con mayor frecuencia de incidentes

### Para el Público:
- ✅ Consultar perros de un dueño específico
- ✅ Visualizar zonas con alta densidad canina
- ✅ Obtener estadísticas de perros por distrito
- ✅ Consultar catálogos de razas y distritos

## Endpoints de la API

### Autenticación
```
POST /api/auth/login
POST /api/auth/register
```

### Gestión de Perros
```
GET    /api/perros                    # Listar perros del usuario
POST   /api/perros                    # Registrar nuevo perro
PUT    /api/perros/{id}              # Actualizar perro
DELETE /api/perros/{id}              # Eliminar perro
GET    /api/perros/{id}              # Obtener perro por ID

# Búsquedas públicas
GET    /api/perros/dueno/{dueno}     # Buscar por dueño
GET    /api/perros/raza/{razaId}     # Buscar por raza
GET    /api/perros/tamaño/{tamaño}   # Buscar por tamaño
GET    /api/perros/comportamiento/{comportamiento} # Buscar por comportamiento
GET    /api/perros/distrito/{distritoId} # Buscar por distrito
GET    /api/perros/densidad/distrito/{distritoId} # Densidad canina por distrito
```

### Gestión de Incidentes
```
GET    /api/incidentes               # Listar todos los incidentes
POST   /api/incidentes               # Reportar incidente
PUT    /api/incidentes/{id}          # Actualizar incidente
DELETE /api/incidentes/{id}          # Eliminar incidente
GET    /api/incidentes/{id}          # Obtener incidente por ID
GET    /api/incidentes/tipo/{tipo}   # Listar por tipo
GET    /api/incidentes/estado/{estado} # Listar por estado
GET    /api/incidentes/perro/{perroId} # Listar por perro

### Estadísticas y Reportes
```
GET    /api/estadisticas/densidad-por-distrito     # Densidad por distrito
GET    /api/estadisticas/perros-por-tamaño         # Perros por tamaño
GET    /api/estadisticas/perros-por-comportamiento # Perros por comportamiento
GET    /api/estadisticas/zonas-alta-densidad       # Zonas con alta densidad
GET    /api/estadisticas/perros-por-dueno          # Perros por dueño
GET    /api/estadisticas/razas-con-incidentes      # Razas con más incidentes
GET    /api/estadisticas/incidentes-por-tipo       # Incidentes por tipo
```

### Catálogos
```
# Razas
GET    /api/catalogo/razas           # Listar razas
POST   /api/catalogo/razas           # Crear raza
PUT    /api/catalogo/razas/{id}      # Actualizar raza
DELETE /api/catalogo/razas/{id}      # Eliminar raza
GET    /api/catalogo/razas/{id}      # Obtener raza por ID

# Distritos
GET    /api/catalogo/distritos       # Listar distritos
POST   /api/catalogo/distritos       # Crear distrito
PUT    /api/catalogo/distritos/{id}  # Actualizar distrito
DELETE /api/catalogo/distritos/{id}  # Eliminar distrito
GET    /api/catalogo/distritos/{id}  # Obtener distrito por ID
```

## Modelos de Datos

### Perro
```json
{
  "id": 1,
  "nombre": "Max",
  "dueño": "Juan Pérez",
  "telefonoDueño": "123456789",
  "emailDueño": "juan@email.com",
  "distritoid": 1,
  "razaid": 1,
  "tamaño": "MEDIANO",
  "comportamiento": "CALMO",
  "color": "Negro",
  "genero": "MACHO",
  "edad": 3,
  "vacunado": true,
  "esterilizado": false,
  "usuarioId": 1,
  "direccion": "Av. Arequipa 123"
}
```

### Incidente
```json
{
  "id": 1,
  "perroId": 1,
  "tipo": "MORDIDA",
  "descripcion": "Mordió a un transeúnte",
  "ubicacion": "Parque Kennedy",
  "fechaIncidente": "2024-01-15T10:30:00",
  "reportadoPor": "María García",
  "telefonoReporte": "987654321",
  "estado": "REPORTADO",
  "fechaReporte": "2024-01-15T11:00:00",
  "usuarioId": 1
}
```

### Raza
```json
{
  "id": 1,
  "nombre": "Labrador Retriever"
}
```

### Distrito
```json
{
  "id": 1,
  "nombre": "Miraflores"
}
```

## Tecnologías Utilizadas

- **Spring Boot 3.x** - Framework principal
- **Spring WebFlux** - Programación reactiva
- **Spring Data R2DBC** - Acceso a datos reactivo
- **Lombok** - Reducción de código boilerplate
- **Reactor** - Programación reactiva

## Instalación y Ejecución

1. Clonar el repositorio
2. Configurar la base de datos en `application.properties`
3. Ejecutar `mvn spring-boot:run`
4. La API estará disponible en `http://localhost:8080`

## Características Técnicas

- ✅ API RESTful completa
- ✅ Programación reactiva con WebFlux
- ✅ Autenticación JWT (simulada)
- ✅ CORS habilitado para frontend
- ✅ Validación de datos
- ✅ Manejo de errores
- ✅ Documentación completa
- ✅ Separación de responsabilidades (MVC)
- ✅ Repositorios reactivos
- ✅ Servicios transaccionales

## Funcionalidades del Sistema

1. **Registro de Perros**: Gestión completa de perros y sus dueños
2. **Catálogos**: Gestión de razas y distritos
3. **Búsquedas**: Filtros por múltiples criterios
4. **Estadísticas**: Análisis de densidad canina y distribución
5. **Incidentes**: Reporte y análisis de incidentes por raza
6. **API Pública**: Acceso sin autenticación para consultas básicas
7. **Geolocalización**: Coordenadas para mapeo de densidad 