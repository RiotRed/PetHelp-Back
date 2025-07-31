# API Endpoints Documentation

## Autenticación

### POST /api/auth/login
- **Descripción**: Iniciar sesión de usuario
- **Body**: `{ "email": "string", "password": "string" }`
- **Response**: `{ "token": "string", "user": {...} }`

### POST /api/auth/register
- **Descripción**: Registrar nuevo usuario
- **Body**: `{ "email": "string", "password": "string", "nombre": "string", "direccion": "string" }`
- **Response**: `{ "token": "string", "user": {...} }`

### GET /api/auth/validate
- **Descripción**: Validar token de autenticación
- **Headers**: `Authorization: Bearer <token>`
- **Response**: `{ "id": number, "email": "string", "nombre": "string", ... }`

## Perros

### GET /api/perros
- **Descripción**: Obtener todos los perros
- **Response**: `Array<Perro>`

### GET /api/perros/{id}
- **Descripción**: Obtener perro por ID
- **Response**: `Perro`

### POST /api/perros
- **Descripción**: Registrar nuevo perro
- **Headers**: `Authorization: Bearer <token>`
- **Body**: `Perro`
- **Response**: `Perro`

### PUT /api/perros/{id}
- **Descripción**: Actualizar perro
- **Headers**: `Authorization: Bearer <token>`
- **Body**: `Perro`
- **Response**: `Perro`

### DELETE /api/perros/{id}
- **Descripción**: Eliminar perro
- **Headers**: `Authorization: Bearer <token>`
- **Response**: `204 No Content`

### GET /api/perros/buscar-dueno?nombre={nombre}
- **Descripción**: Buscar perros por dueño
- **Response**: `Array<Perro>`

### GET /api/perros/raza?raza={raza}
- **Descripción**: Buscar perros por raza
- **Response**: `Array<Perro>`

### GET /api/perros/tamaño?tamaño={tamaño}
- **Descripción**: Buscar perros por tamaño
- **Response**: `Array<Perro>`

### GET /api/perros/comportamiento?comportamiento={comportamiento}
- **Descripción**: Buscar perros por comportamiento
- **Response**: `Array<Perro>`

### GET /api/perros/ubicacion?ubicacion={ubicacion}
- **Descripción**: Buscar perros por ubicación
- **Response**: `Array<Perro>`

### GET /api/perros/densidad
- **Descripción**: Obtener mapa de densidad canina por distrito
- **Response**: `{ "densidadPorDistrito": {...} }`

### GET /api/perros/estadisticas
- **Descripción**: Obtener estadísticas generales de perros
- **Response**: `{ "totalPerros": number, "porTamaño": {...}, "porComportamiento": {...} }`

## Incidentes

### GET /api/incidentes
- **Descripción**: Obtener todos los incidentes
- **Response**: `Array<Incidente>`

### GET /api/incidentes/{id}
- **Descripción**: Obtener incidente por ID
- **Response**: `Incidente`

### POST /api/incidentes
- **Descripción**: Reportar nuevo incidente
- **Headers**: `Authorization: Bearer <token>`
- **Body**: `Incidente`
- **Response**: `Incidente`

### PUT /api/incidentes/{id}
- **Descripción**: Actualizar incidente
- **Headers**: `Authorization: Bearer <token>`
- **Body**: `Incidente`
- **Response**: `Incidente`

### DELETE /api/incidentes/{id}
- **Descripción**: Eliminar incidente
- **Headers**: `Authorization: Bearer <token>`
- **Response**: `204 No Content`

### GET /api/incidentes/estadisticas
- **Descripción**: Obtener estadísticas de incidentes
- **Response**: `{ "totalIncidentes": number, "porTipo": {...}, "porEstado": {...} }`

### GET /api/incidentes/tipo?tipo={tipo}
- **Descripción**: Listar incidentes por tipo
- **Response**: `Array<Incidente>`

### GET /api/incidentes/estado?estado={estado}
- **Descripción**: Listar incidentes por estado
- **Response**: `Array<Incidente>`

### GET /api/incidentes/perro/{perroId}
- **Descripción**: Listar incidentes de un perro específico
- **Response**: `Array<Incidente>`

## Distritos

### GET /api/distritos
- **Descripción**: Obtener todos los distritos
- **Response**: `Array<Distrito>`

### GET /api/distritos/{id}
- **Descripción**: Obtener distrito por ID
- **Response**: `Distrito`

### POST /api/distritos
- **Descripción**: Crear nuevo distrito
- **Headers**: `Authorization: Bearer <token>`
- **Body**: `Distrito`
- **Response**: `Distrito`

### PUT /api/distritos/{id}
- **Descripción**: Actualizar distrito
- **Headers**: `Authorization: Bearer <token>`
- **Body**: `Distrito`
- **Response**: `Distrito`

### DELETE /api/distritos/{id}
- **Descripción**: Eliminar distrito
- **Headers**: `Authorization: Bearer <token>`
- **Response**: `204 No Content`

## Razas

### GET /api/razas
- **Descripción**: Obtener todas las razas
- **Response**: `Array<Raza>`

### GET /api/razas/{id}
- **Descripción**: Obtener raza por ID
- **Response**: `Raza`

### POST /api/razas
- **Descripción**: Crear nueva raza
- **Headers**: `Authorization: Bearer <token>`
- **Body**: `Raza`
- **Response**: `Raza`

### PUT /api/razas/{id}
- **Descripción**: Actualizar raza
- **Headers**: `Authorization: Bearer <token>`
- **Body**: `Raza`
- **Response**: `Raza`

### DELETE /api/razas/{id}
- **Descripción**: Eliminar raza
- **Headers**: `Authorization: Bearer <token>`
- **Response**: `204 No Content`

## Modelos de Datos

### Usuario
```json
{
  "id": "number",
  "email": "string",
  "nombre": "string",
  "direccion": "string",
  "password": "string",
  "dueño": "boolean"
}
```

### Perro
```json
{
  "id": "number",
  "nombre": "string",
  "distritoid": "number",
  "razaid": "number",
  "tamanio": "string",
  "comportamiento": "string",
  "color": "string",
  "genero": "string",
  "edad": "number",
  "vacunado": "boolean",
  "esterilizado": "boolean",
  "usuarioId": "number",
  "direccion": "string"
}
```

### Incidente
```json
{
  "id": "number",
  "perroId": "number",
  "tipo": "string",
  "descripcion": "string",
  "ubicacion": "string",
  "fechaIncidente": "datetime",
  "reportadoPor": "string",
  "telefonoReporte": "string",
  "estado": "string",
  "fechaReporte": "datetime",
  "usuarioId": "number"
}
```

### Distrito
```json
{
  "id": "number",
  "nombre": "string"
}
```

### Raza
```json
{
  "id": "number",
  "nombre": "string"
}
```

## Códigos de Estado HTTP

- `200 OK`: Operación exitosa
- `201 Created`: Recurso creado exitosamente
- `204 No Content`: Operación exitosa sin contenido
- `400 Bad Request`: Datos de entrada inválidos
- `401 Unauthorized`: No autorizado
- `404 Not Found`: Recurso no encontrado
- `500 Internal Server Error`: Error interno del servidor 