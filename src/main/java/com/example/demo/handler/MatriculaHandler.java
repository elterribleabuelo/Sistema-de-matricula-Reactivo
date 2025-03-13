package com.example.demo.handler;

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
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor

public class MatriculaHandler {

    private final IMatriculaService serviceMatricula;
    private final IEstudianteService serviceEstudiante;
    private final ICursoService serviceCurso;
    private final ModelMapper modelMapper;

    public Mono<ServerResponse> save(ServerRequest request){

        Mono<MatriculaDTO> monoInvoiceDTO = request.bodyToMono(MatriculaDTO.class);

        return  request.bodyToMono(MatriculaDTO.class)
                .flatMap(matriculaDTO -> {

                    String idEstudiante = matriculaDTO.getEstudiante().getId();
                    List<String> idCursos = matriculaDTO.getCursos().stream().map(CursoDTO::getId).toList();

                    Mono<Estudiante> estudianteMono = serviceEstudiante.findById(idEstudiante);
                    Mono<List<Curso>> cursosMono = serviceCurso.findAllById(idCursos).collectList();

                    return Mono.zip(estudianteMono,cursosMono)
                            .flatMap(tupla -> {

                                Estudiante estudiante = tupla.getT1();

                                Estudiante estudianteCurso = new Estudiante(estudiante.getId(),estudiante.getNombres(),estudiante.getApellidos());

                                EstudianteDTO estudianteDTO = modelMapper.map(estudianteCurso,EstudianteDTO.class);

                                List<Curso> cursos = tupla.getT2();

                                List<CursoDTO> cursosDTO = cursos.stream().map(curso->new CursoDTO(curso.getId(),curso.getNombre())).collect(Collectors.toList());

                                matriculaDTO.setEstudiante(estudianteDTO);
                                matriculaDTO.setCursos(cursosDTO);

                                return serviceMatricula.save(convertToDocument(matriculaDTO));
                            })
                            .map(this::convertToDto)
                            .flatMap(e -> ServerResponse.created(URI.create(request.uri().toString().concat("/").concat(e.getId())))
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .bodyValue(new GenericResponseDTO(201,"success",e)));
                });
    }


    private MatriculaDTO convertToDto(Matricula model) {
        return modelMapper.map(model, MatriculaDTO.class);
    }

    private Matricula convertToDocument(MatriculaDTO dto) {
        return modelMapper.map(dto, Matricula.class);
    }
}
