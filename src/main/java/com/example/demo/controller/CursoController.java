package com.example.demo.controller;

import com.example.demo.dto.CursoDTO;
import com.example.demo.dto.GenericResponseDTO;
import com.example.demo.exception.CustomErrorResponse;
import com.example.demo.model.Curso;
import com.example.demo.service.ICursoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/cursos")
@RequiredArgsConstructor

public class CursoController {

    private final ICursoService service;
    private final ModelMapper modelMapper;


    @PostMapping
    public Mono<ResponseEntity<GenericResponseDTO>> save(@Valid @RequestBody CursoDTO curso, final ServerHttpRequest req) {

        return service.existsCursoByNombre(curso.getNombre())
                .flatMap(exists -> {
                    if(exists){
                        CustomErrorResponse error = new CustomErrorResponse(LocalDateTime.now(),"El nombre del curso ya está registrado",String.valueOf(req.getURI()));
                        return Mono.just(ResponseEntity.badRequest().body(new GenericResponseDTO(400,"not-found", List.of(error))));
                    }else{
                        return service.save(convertToDocument(curso))
                                .map(this::convertToDto)
                                .map(e -> ResponseEntity.created(
                                                URI.create(req.getURI().toString().concat("/").concat(e.getId())))
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .body(new GenericResponseDTO(201,"success",e)))
                                .defaultIfEmpty(ResponseEntity.notFound().build());
                    }
                });
    }

    @GetMapping
    public Mono<ResponseEntity<GenericResponseDTO>> findAll() {

        Flux<CursoDTO> fx = service.findAll().map(this::convertToDto);

        return service.findAll()
                .map(this::convertToDto)
                .collectList()
                .map(e -> ResponseEntity.ok()
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(new GenericResponseDTO(200,"success",e)))
                            .defaultIfEmpty(ResponseEntity.notFound().build());

    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<GenericResponseDTO>> findById(@PathVariable("id") String id) {

        Mono<CursoDTO> fx = service.findById(id).map(this::convertToDto);

        return service.findById(id)
                      .map(this::convertToDto)
                      .map(e-> ResponseEntity.ok()
                              .contentType(MediaType.APPLICATION_JSON)
                              .body(new GenericResponseDTO(200,"success",e)))
                              .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<GenericResponseDTO>> update(@Valid @PathVariable("id") String id, @RequestBody CursoDTO curso) {

        return Mono.just(curso)
                .map(e -> {
                    e.setId(id);
                    return e;
                })
                .flatMap(e -> service.update(id, convertToDocument(e)))
                .map(this::convertToDto)
                .map(e -> ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(new GenericResponseDTO(200,"success",e))
                ).defaultIfEmpty(ResponseEntity.notFound().build());

    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> delete(@PathVariable("id") String id) {
        return service.delete(id)
                .flatMap(result -> {
                    if (result) {
                        return Mono.just(ResponseEntity.noContent().build());
                    } else {
                        return Mono.just(ResponseEntity.notFound().build());
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
