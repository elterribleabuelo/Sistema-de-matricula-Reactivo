# Sistema-de-matricula-Reactivo

Este repositorio contiene el código fuente de los servicios REST para el CRUD de estudiante y curso , así como el registro de matrícula de una academia usando progrmación reactiva y Spring Web Flux.

Los endpoints desarrollados son:

**Estudiantes:**

CRUD y listado de estudiantes en forma descendente usando la edad.

- /estudiantes (GET,POST) --> Anotacional | /v2/estudiantes (GET,POST) --> Funcional
- /estudiantes/{id} (GET,PUT,DELETE) --> Anotacional | /estudiantes/{id} (GET,PUT,DELETE) --> Funcional

### `Body request POST/PUT`

```Body request POST/PUT
{
    "nombres":"Antonio",
    "apellidos":"Salazar",
    "dni":"45634021",
    "edad":26
}
```

- /estudiantes/ordenadoAscPorEdad (GET) --> Anotacional | /v2/estudiantes/ordenadoAscPorEdad (GET) --> Funcional
- /estudiantes/ordenadoDescPorEdad (GET) --> Anotacional | /v2/estudiantes/ordenadoDescPorEdad (GET) --> Funcional

**Cursos**

CRUD

- /cursos(GET,POST) --> Anotacional | /v2/cursos (GET,POST) --> Funcional
- /cursos/{id} (GET,PUT,DELETE) --> Anotacional | /v2/cursos/{id} (GET,PUT,DELETE) --> Funcional

### `Body request POST/PUT`

```Body request POST/PUT
{
    "nombre":".NET 8 Full Stack",
    "siglas":"NETFS",
    "estado":true
}
```

**Matricula**

Creacion/lectura de matriculas y relación de cursos matriculados y sus estudiantes correspondientes.

- /matricula (POST) --> Anotacional | /v2/matricula --> Funcional

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
            "datetime": "2024-11-28T17:47:17.781305",
            "message": "ID NOT FOUND 10000",
            "path": "uri=/estudiantes/10000"
        }
    ]
}
```

- Registro de curso con una cantidad de siglas menor a 3

### `Response`

```Response
{
    "datetime": "2025-03-10T22:19:25.7185602",
    "message": "siglas: el tamaño debe estar entre 3 y 20",
    "status": 400
}
```

- Registro de estudiante con un nombre que contiene caracteres distintos a letras-acento/espacios en blanco

### `Response`

```Response
{
    "datetime": "2025-03-11T00:34:56.2882672",
    "message": "apellidos: Debe contener solo letras y espacios",
    "status": 400
}
```

- Registro de estudiante con un DNI contiene caracteres distintos a números

### `Response`

```Response
{
    "datetime": "2025-03-11T00:31:39.8168248",
    "message": "dni: Debe contener solo números",
    "status": 400
}
```

<h2 style="color:red;">Validaciones adicionales</h2>

- Registro de estudiante con DNI repetido

### `Response`

```Response
{
    "status": 400,
    "message": "not-found",
    "data": [
        {
            "datetime": "2025-03-11T00:37:08.3887575",
            "message": "El DNI ya está registrado",
            "status": 400
        }
    ]
}
```

- Registro de curso con nombre repetido

### `Response`

```Response
{
    "status": 400,
    "message": "not-found",
    "data": [
        {
            "datetime": "2025-03-11T00:38:49.9768856",
            "message": "El nombre del curso ya está registrado",
            "status": 400
        }
    ]
}
```
