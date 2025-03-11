package com.example.demo.dto;

import com.example.demo.model.Curso;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor

@JsonInclude(JsonInclude.Include.NON_NULL)

public class MatriculaDTO {

    private String id;

    @NotNull
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}$", message = "El formato debe ser 'yyyy-MM-ddTHH:mm:ss'")
    private LocalDateTime fechaMatricula;

    @NotNull
    private EstudianteDTO estudiante;

    @NotNull
    private List<CursoDTO> cursos;

    @NotNull
    private Boolean estado;
}
