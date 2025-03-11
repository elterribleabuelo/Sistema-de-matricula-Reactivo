package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Document(collection = "estudiantes")

public class Estudiante {

    @Id
    @EqualsAndHashCode.Include
    private String id;

    @Field
    private String nombres;

    @Field
    private String apellidos;

    @Field
    private String dni;

    @Field
    private Integer edad;

    public Estudiante(String id, String nombres, String apellidos) {
        this.id = id;
        this.nombres=nombres;
        this.apellidos=apellidos;
    }
}
