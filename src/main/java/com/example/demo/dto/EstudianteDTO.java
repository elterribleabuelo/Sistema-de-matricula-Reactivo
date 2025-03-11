package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

@JsonInclude(JsonInclude.Include.NON_NULL)

public class EstudianteDTO {

    private String id;

    @NotNull
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$", message = "Debe contener solo letras y espacios")
    private String nombres;

    @NotNull
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$", message = "Debe contener solo letras y espacios")
    private String apellidos;

    @NotNull
    @Pattern(regexp = "[0-9]+",message = "Debe contener solo números")
    private String dni;

    @NotNull
    @Min(value = 1)
    private Integer edad;

}
