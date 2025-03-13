# Sistema-de-matricula-Reactivo

Este repositorio contiene el código fuente de los servicios REST para el CRUD de estudiante y curso , así como el registro de matrícula de una academia usando programación reactiva y Spring Web Flux.

Los endpoints desarrollados son:

**Estudiantes:**

CRUD y listado de estudiantes en forma ascendente/descendente usando la edad.

***Anotacional***

- /estudiantes (GET,POST)
- /estudiantes/{id} (GET,PUT,DELETE)
- /estudiantes/ordenadoAscPorEdad (GET)
- /estudiantes/ordenadoDescPorEdad (GET)

***Funcional***

- /v2/estudiantes (GET,POST)
- /v2/estudiantes/{id} (GET,PUT,DELETE)
- /v2/estudiantes/ordenadoAscPorEdad (GET)
- /v2/estudiantes/ordenadoDescPorEdad (GET)

### `Body request POST/PUT`

```Body request POST/PUT
{
    "nombres":"Antonio",
    "apellidos":"Salazar",
    "dni":"45634021",
    "edad":26
}
```

**Cursos**

CRUD

***Anotacional***
- /cursos(GET,POST)
-  /cursos/{id} (GET,PUT,DELETE)

***Funcional***
- /v2/cursos (GET,POST)
- /v2/cursos/{id} (GET,PUT,DELETE)

### `Body request POST/PUT`

```Body request POST/PUT
{
    "nombre":".NET 8 Full Stack",
    "siglas":"NETFS",
    "estado":true
}
```

**Matricula**

Registro de matriculas.

***Anotacional***

- /matricula (POST)

***Funcional***

- /v2/matricula (POST)

### `Body request POST`

```Body request POST
{
    "fechaMatricula":"2025-03-09T13:00:20",
    "estudiante":{"id":"67c7dd398e1a764a0d7303dd"},
    "cursos":[
        {"id":"67cdd5c8b150552adad7c49c"},
        {"id":"67cdd613b150552adad7c49d"},
        {"id":"67cdd66bb150552adad7c49f"}
    ],
    "estado":true
}
```

<h2 style="color:red;">Control de excepciones</h2>

- Actualizacion de estudiantes y cursos con ID no registrado

### `Response`

```Response
{
    "status": 404,
    "message": "not-found",
    "data": [
        {
            "datetime": "2025-03-13T16:57:40.1749614",
            "message": "ID NOT FOUND:67d353da00660f170edb07aff",
            "path": "http://localhost:8080/cursos/67d353da00660f170edb07aff"
        }
    ]
}
```

- Registro de curso con una cantidad de siglas menor a 3

### `Response`

```Response
{
    "status": 400,
    "message": "bad-request",
    "data": [
        {
            "datetime": "2025-03-13T13:06:23.4849308",
            "message": "siglas: el tamaño debe estar entre 3 y 7",
            "path": "http://localhost:8080/cursos"
        }
    ]
}
```

- Registro de estudiante con un nombre que contiene caracteres distintos a letras-acento/espacios en blanco

### `Response`

```Response
{
    "status": 400,
    "message": "bad-request",
    "data": [
        {
            "datetime": "2025-03-13T13:22:40.6436279",
            "message": "apellidos: Debe contener solo letras y espacios",
            "path": "http://localhost:8080/estudiantes"
        }
    ]
}
```

- Registro de estudiante con un DNI contiene caracteres distintos a números

### `Response`

```Response
{
    "status": 400,
    "message": "bad-request",
    "data": [
        {
            "datetime": "2025-03-13T13:24:18.0486151",
            "message": "dni: Debe contener solo números",
            "path": "http://localhost:8080/estudiantes"
        }
    ]
}
```

<h2 style="color:red;">Validaciones adicionales</h2>

- Registro de estudiante con DNI repetido

### `Response`

```Response
{
    "status": 400,
    "message": "bad-request",
    "data": [
        {
            "datetime": "2025-03-13T13:32:53.5483936",
            "message": "El DNI ya está registrado",
            "path": "http://localhost:8080/estudiantes"
        }
    ]
}
```

- Registro de curso con nombre repetido

### `Response`

```Response
{
    "status": 400,
    "message": "bad-request",
    "data": [
        {
            "datetime": "2025-03-13T13:33:29.8248911",
            "message": "El nombre del curso ya está registrado",
            "path": "http://localhost:8080/cursos"
        }
    ]
}
```

<h2 style="color:blue;">Consideraciones</h2>

La aplicación está protegida con el uso de JWT, por lo cual para consumir los servicios
se necesita autenticarse.

La ruta a consultar es la siguiente: 
- /login (POST)

Para la ruta en cuestión usar el siguiente body :

```Body request POST
{
    "username" : "mitocode",
    "password" : "123"
}
```
y además crear una colección users en MongoDB con el siguiente registro:

```Registro en BD
{
    "_id" : ObjectId("67a01b6803ab3a31fcf2c836"),
    "username" : "mitocode",
    "password" : "$2a$12$bwmRR2qx59lTwHQXH3WwmOgVeRJ0F1ex9mTixbXuDv3G0zxaZwymC",
    "status" : true,
    "roles" : [
        {
            "_id" : ObjectId("67a01b6803ab3a31fcf2c826")
        }
    ]
}
```



