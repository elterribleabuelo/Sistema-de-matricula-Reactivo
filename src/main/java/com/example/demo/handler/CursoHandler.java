package com.example.demo.handler;

import com.example.demo.dto.CursoDTO;
import com.example.demo.dto.GenericResponseDTO;
import com.example.demo.exception.CustomErrorResponse;
import com.example.demo.model.Curso;
import com.example.demo.service.ICursoService;
import com.example.demo.validator.RequestValidator;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.web.reactive.function.BodyInserters.fromValue;

@Component
@RequiredArgsConstructor

public class CursoHandler {

    private final ICursoService service;
    private final ModelMapper modelMapper;
    private final RequestValidator requestValidator;

    public Mono<ServerResponse> save(ServerRequest request){

        return request.bodyToMono(CursoDTO.class)
                        .flatMap(cursoDTO -> {
                            String nombre = cursoDTO.getNombre();
                            return service.existsCursoByNombre(nombre)
                                    .flatMap(exists->{
                                        if(exists){
                                            CustomErrorResponse error = new CustomErrorResponse(LocalDateTime.now(),"El nombre ya está en uso", HttpStatus.BAD_REQUEST.value());
                                            return ServerResponse.status(400)
                                                    .contentType(MediaType.APPLICATION_JSON)
                                                    .bodyValue(new GenericResponseDTO(400,"not-found", List.of(error)));
                                        }else{
                                            return  Mono.just(cursoDTO)
                                                    .flatMap(requestValidator::validate)
                                                    .flatMap(e->service.save(convertToDocument(e))
                                                    .map(this::convertToDto)
                                                    .flatMap(savedCursoDTO->{
                                                        GenericResponseDTO responseDTO = new GenericResponseDTO(200,"success",cursoDTO);
                                                        return  ServerResponse
                                                                .created(URI.create(request.uri().toString().concat("/").concat(savedCursoDTO.getId())))
                                                                .contentType(MediaType.APPLICATION_JSON)
                                                                .body(fromValue(responseDTO));

                                                    }));
                                        }
                                    });
                        });
    }

    public Mono<ServerResponse> update(ServerRequest request){

        String id = request.pathVariable("id");
        Mono<CursoDTO> monoCursoDTO = request.bodyToMono(CursoDTO.class);

        return monoCursoDTO
                .map(e->{
                    e.setId(id);
                    return e;
                })
                .flatMap(requestValidator::validate)
                .flatMap(e->service.update(id,convertToDocument(e)))
                .map(this::convertToDto)
                .flatMap(e -> ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(new GenericResponseDTO(200,"success",e))
                );
    }

    public Mono<ServerResponse> findAll(ServerRequest request){

        Flux<CursoDTO> cursos = service.findAll().map(this::convertToDto);
        Mono<List<CursoDTO>> cursosDTO = cursos.collectList();

        return  cursosDTO.map(curso -> new GenericResponseDTO(200,"success",curso))
                         .flatMap(responseDTO -> ServerResponse
                                                .ok()
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .bodyValue(responseDTO)
                         );
    }

    public Mono<ServerResponse> findById(ServerRequest request){

        String id = request.pathVariable("id");

        Mono<CursoDTO> cursoDTO = service.findById(id).map(this::convertToDto);

        return cursoDTO.map(c-> new GenericResponseDTO(200,"success",c))
                        .flatMap(responseDTO -> ServerResponse
                                .ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(responseDTO)
                        ).switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> delete(ServerRequest request){
        String id = request.pathVariable("id");

        return service.delete(id)
                .flatMap(result->{
                    if(result){
                        return ServerResponse.noContent().build();
                    }else{
                        return ServerResponse.notFound().build();
                    }
                });
    }

    private CursoDTO convertToDto(Curso model) {
        return modelMapper.map(model, CursoDTO.class);
    }

    private Curso convertToDocument(CursoDTO dto) {
        return modelMapper.map(dto, Curso.class);
    }


}
