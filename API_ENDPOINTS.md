# PetHelp Backend API Documentation

## Base URL
```
http://localhost:8080/api
```

## Authentication

### Login
- **POST** `/auth/login`
- **Body:**
```json
{
  "email": "user@example.com",
  "password": "password123"
}
```
- **Response:**
```json
{
  "token": "fake-jwt-token-1",
  "user": {
    "id": 1,
    "email": "user@example.com",
    "nombre": "John Doe",
    "direccion": "123 Main St",
    "dueño": true
  }
}
```

### Register
- **POST** `/auth/register`
- **Body:**
```json
{
  "email": "newuser@example.com",
  "password": "password123",
  "nombre": "New User",
  "direccion": "456 Oak St"
}
```
- **Response:** Same as login

### Validate Token
- **GET** `/auth/validate`
- **Headers:** `Authorization: Bearer fake-jwt-token-1`
- **Response:**
```json
{
  "id": 1,
  "email": "user@example.com",
  "nombre": "John Doe",
  "direccion": "123 Main St",
  "dueño": true
}
```

## Perros (Dogs)

### Get All Dogs
- **GET** `/perros`
- **Response:** Array of dog objects

### Get Dog by ID
- **GET** `/perros/{id}`
- **Response:** Single dog object

### Create Dog
- **POST** `/perros`
- **Headers:** `Authorization: Bearer fake-jwt-token-1`
- **Body:**
```json
{
  "nombre": "Buddy",
  "distritoid": 1,
  "razaid": 1,
  "tamanio": "Mediano",
  "comportamiento": "Amigable",
  "color": "Marrón",
  "genero": "Macho",
  "edad": 3,
  "vacunado": true,
  "esterilizado": false,
  "direccion": "123 Main St"
}
```

### Update Dog
- **PUT** `/perros/{id}`
- **Headers:** `Authorization: Bearer fake-jwt-token-1`
- **Body:** Same as create

### Delete Dog
- **DELETE** `/perros/{id}`
- **Headers:** `Authorization: Bearer fake-jwt-token-1`

### Search Dogs by Owner
- **GET** `/perros/buscar-dueno?nombre=John`
- **Response:** Array of dogs

### Search Dogs by Breed
- **GET** `/perros/raza?raza=1`
- **Response:** Array of dogs

### Search Dogs by Size
- **GET** `/perros/tamaño?tamaño=Mediano`
- **Response:** Array of dogs

### Search Dogs by Behavior
- **GET** `/perros/comportamiento?comportamiento=Amigable`
- **Response:** Array of dogs

### Search Dogs by Location
- **GET** `/perros/ubicacion?ubicacion=Main St`
- **Response:** Array of dogs

### Get Density Map
- **GET** `/perros/densidad`
- **Response:**
```json
{
  "densidadPorDistrito": {
    "1": 15,
    "2": 8,
    "3": 12
  }
}
```

### Get General Statistics
- **GET** `/perros/estadisticas`
- **Response:**
```json
{
  "totalPerros": 35,
  "porTamaño": {
    "Pequeño": 10,
    "Mediano": 15,
    "Grande": 10
  },
  "porComportamiento": {
    "Amigable": 20,
    "Agresivo": 5,
    "Tímido": 10
  }
}
```

## Razas (Breeds)

### Get All Breeds
- **GET** `/razas`
- **Response:** Array of breed objects

### Get Breed by ID
- **GET** `/razas/{id}`
- **Response:** Single breed object

### Create Breed
- **POST** `/razas`
- **Headers:** `Authorization: Bearer fake-jwt-token-1`
- **Body:**
```json
{
  "nombre": "Golden Retriever",
  "descripcion": "Perro amigable y leal"
}
```

### Update Breed
- **PUT** `/razas/{id}`
- **Headers:** `Authorization: Bearer fake-jwt-token-1`

### Delete Breed
- **DELETE** `/razas/{id}`
- **Headers:** `Authorization: Bearer fake-jwt-token-1`

## Distritos (Districts)

### Get All Districts
- **GET** `/distritos`
- **Response:** Array of district objects

### Get District by ID
- **GET** `/distritos/{id}`
- **Response:** Single district object

### Create District
- **POST** `/distritos`
- **Headers:** `Authorization: Bearer fake-jwt-token-1`
- **Body:**
```json
{
  "nombre": "Miraflores",
  "descripcion": "Distrito turístico"
}
```

### Update District
- **PUT** `/distritos/{id}`
- **Headers:** `Authorization: Bearer fake-jwt-token-1`

### Delete District
- **DELETE** `/distritos/{id}`
- **Headers:** `Authorization: Bearer fake-jwt-token-1`

## Incidentes (Incidents)

### Get All Incidents
- **GET** `/incidentes`
- **Response:** Array of incident objects

### Get Incident by ID
- **GET** `/incidentes/{id}`
- **Response:** Single incident object

### Create Incident
- **POST** `/incidentes`
- **Headers:** `Authorization: Bearer fake-jwt-token-1`
- **Body:**
```json
{
  "tipo": "Mordedura",
  "descripcion": "Perro mordió a una persona",
  "fecha": "2024-01-15",
  "ubicacion": "Parque Kennedy",
  "estado": "Reportado",
  "perroId": 1
}
```

### Update Incident
- **PUT** `/incidentes/{id}`
- **Headers:** `Authorization: Bearer fake-jwt-token-1`

### Delete Incident
- **DELETE** `/incidentes/{id}`
- **Headers:** `Authorization: Bearer fake-jwt-token-1`

### Get Incident Statistics
- **GET** `/incidentes/estadisticas`
- **Response:**
```json
{
  "totalIncidentes": 25,
  "porTipo": {
    "Mordedura": 10,
    "Ataque": 5,
    "Otro": 10
  },
  "porEstado": {
    "Reportado": 15,
    "En Investigación": 5,
    "Resuelto": 5
  }
}
```

### Get Incidents by Type
- **GET** `/incidentes/tipo?tipo=Mordedura`
- **Response:** Array of incidents

### Get Incidents by Status
- **GET** `/incidentes/estado?estado=Reportado`
- **Response:** Array of incidents

### Get Incidents by Dog
- **GET** `/incidentes/perro/{perroId}`
- **Response:** Array of incidents

## Error Responses

All endpoints return consistent error responses:

```json
{
  "success": false,
  "message": "Error description",
  "error": "ErrorType"
}
```

## CORS Configuration

The API is configured to accept requests from:
- `http://localhost:3000`
- `http://localhost:5173`
- `http://localhost:4173`

## Authentication

All protected endpoints require the `Authorization` header with a Bearer token:
```
Authorization: Bearer fake-jwt-token-{userId}
```

The current implementation uses fake JWT tokens for simplicity. In production, implement proper JWT tokens with expiration and signature validation. 