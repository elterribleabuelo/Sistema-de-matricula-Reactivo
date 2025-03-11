package com.example.demo.controller;

import com.example.demo.dto.CursoDTO;
import com.example.demo.dto.EstudianteDTO;
import com.example.demo.dto.GenericResponseDTO;
import com.example.demo.dto.MatriculaDTO;
import com.example.demo.model.Curso;
import com.example.demo.model.Estudiante;
import com.example.demo.model.Matricula;
import com.example.demo.service.ICursoService;
import com.example.demo.service.IEstudianteService;
import com.example.demo.service.IMatriculaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequiredArgsConstructor
@RequestMapping("/matriculas")

public class MatriculaController {

    private final IMatriculaService serviceMatricula;
    private final IEstudianteService serviceEstudiante;
    private final ICursoService serviceCurso;
    private final ModelMapper modelMapper;
    private EstudianteDTO estudianteAux;
    private List<CursoDTO> cursosListAux;

    private static final Logger log = LoggerFactory.getLogger(MatriculaController.class);

    @PostMapping
    public Mono<ResponseEntity<GenericResponseDTO>> save(@Valid @RequestBody MatriculaDTO matricula, final ServerHttpRequest req) {

        log.info("MatriculaDTO inicial: {}", matricula);

        // Obteniendo id de estudiante a través de lo que viene en el request
        String idEstudiante = matricula.getEstudiante().getId();

        // Obteniendo id de los cursos a través de lo que viene en el request
        List<String> idCursos = matricula.getCursos().stream().map(CursoDTO::getId).toList();

        // Buscando estudiante en BD
        Mono<Estudiante> estudianteMono = serviceEstudiante.findById(idEstudiante);

        // Buscando cursos en BD
        Mono<List<Curso>> cursosMono = serviceCurso.findAllById(idCursos).collectList();

        return Mono.zip(estudianteMono,cursosMono)
                    .flatMap(tupla -> {

                        Estudiante estudiante = tupla.getT1();

                        Estudiante estudianteCurso = new Estudiante(estudiante.getId(),estudiante.getNombres(),estudiante.getApellidos());

                        EstudianteDTO estudianteDTO = modelMapper.map(estudianteCurso,EstudianteDTO.class);

                        List<Curso> cursos = tupla.getT2();

                        List<CursoDTO> cursosDTO = cursos.stream().map(curso->new CursoDTO(curso.getId(),curso.getNombre())).collect(Collectors.toList());

                        matricula.setEstudiante(estudianteDTO);
                        matricula.setCursos(cursosDTO);

                        return serviceMatricula.save(convertToDocument(matricula));
                    })
                    .map(this::convertToDto)
                    .map(e -> ResponseEntity.created(
                                    URI.create(req.getURI().toString().concat("/").concat(e.getId())))
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(new GenericResponseDTO(201,"success",e)))
                    .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    private MatriculaDTO convertToDto(Matricula model) {
        return modelMapper.map(model, MatriculaDTO.class);
    }

    private Matricula convertToDocument(MatriculaDTO dto) {
        return modelMapper.map(dto, Matricula.class);
    }

}
