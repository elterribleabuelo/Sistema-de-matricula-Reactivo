package com.example.demo.handler;

import com.example.demo.dto.EstudianteDTO;
import com.example.demo.dto.GenericResponseDTO;
import com.example.demo.exception.CustomErrorResponse;
import com.example.demo.model.Estudiante;
import com.example.demo.service.IEstudianteService;
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

public class EstudianteHandler {

    private final IEstudianteService service;
    private final ModelMapper modelMapper;
    private final RequestValidator requestValidator;

    public Mono<ServerResponse> save(ServerRequest request){

        return request.bodyToMono(EstudianteDTO.class)
                .flatMap(estudianteDTO -> {
                    String dni = estudianteDTO.getDni();
                    return service.existsEstudianteByDni(dni)
                            .flatMap(exists->{
                                if(exists){
                                    CustomErrorResponse error = new CustomErrorResponse(LocalDateTime.now(),"El DNI ya está registrado", HttpStatus.BAD_REQUEST.value());
                                    return ServerResponse.status(400)
                                            .contentType(MediaType.APPLICATION_JSON)
                                            .bodyValue(new GenericResponseDTO(400,"not-found", List.of(error)));
                                }else{
                                    return  Mono.just(estudianteDTO)
                                                .flatMap(requestValidator::validate)
                                                .flatMap(e->service.save(convertToDocument(e)))
                                                .map(this::convertToDto)
                                                .flatMap(savedEstudianteDTO->{
                                                    GenericResponseDTO responseDTO = new GenericResponseDTO(200,"success",savedEstudianteDTO);
                                                    return  ServerResponse
                                                            .created(URI.create(request.uri().toString().concat("/").concat(savedEstudianteDTO.getId())))
                                                            .contentType(MediaType.APPLICATION_JSON)
                                                            .body(fromValue(responseDTO));

                                                });
                                }
                            });
                });
    }

    public Mono<ServerResponse> update(ServerRequest request){

        String id = request.pathVariable("id");
        Mono<EstudianteDTO> monoEstudianteDTO = request.bodyToMono(EstudianteDTO.class);

        return monoEstudianteDTO
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

        Flux<EstudianteDTO> estudiantes = service.findAll().map(this::convertToDto);
        Mono<List<EstudianteDTO>> estudiantesDTO = estudiantes.collectList();

        return  estudiantesDTO.map(curso -> new GenericResponseDTO(200,"success",curso))
                .flatMap(responseDTO -> ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(responseDTO)
                );
    }

    public Mono<ServerResponse> findById(ServerRequest request){

        String id = request.pathVariable("id");

        Mono<EstudianteDTO> estudianteDTO = service.findById(id).map(this::convertToDto);

        return estudianteDTO.map(c-> new GenericResponseDTO(200,"success",c))
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

    public Mono<ServerResponse> findAllByOrderByEdadAsc(ServerRequest request){
        return service.findAllByOrderByEdadAsc()
                .map(this::convertToDto)
                .collectList()
                .map(estudiantes -> new GenericResponseDTO(200,"success",estudiantes))
                .flatMap(responseDTO -> ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(responseDTO));
    }

    public Mono<ServerResponse> findAllByOrderByEdadDesc(ServerRequest request){
       return  service.findAllByOrderByEdadDesc()
                .map(this::convertToDto)
                .collectList()
                .map(estudiantes -> new GenericResponseDTO(200,"success",estudiantes))
                .flatMap(responseDTO -> ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(responseDTO));
    }

    private EstudianteDTO convertToDto(Estudiante model) {
        return modelMapper.map(model, EstudianteDTO.class);
    }

    private Estudiante convertToDocument(EstudianteDTO dto) {
        return modelMapper.map(dto, Estudiante.class);
    }

}
