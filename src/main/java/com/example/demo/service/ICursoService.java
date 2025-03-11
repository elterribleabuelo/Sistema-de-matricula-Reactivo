package com.example.demo.service;

import com.example.demo.model.Curso;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ICursoService extends ICRUD<Curso,String>{
    Mono<Boolean> existsCursoByNombre(String nombre);
    Flux<Curso> findAllById(List<String> id);
}
