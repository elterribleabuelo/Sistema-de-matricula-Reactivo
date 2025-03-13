package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

@JsonInclude(JsonInclude.Include.NON_NULL)

public class CursoDTO {

    private String id;

    @NotNull
    private String nombre;

    @NotNull
    @Size(min = 3, max = 7)
    private String siglas;

    @NotNull
    private Boolean estado;

    public CursoDTO(String id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }
}
