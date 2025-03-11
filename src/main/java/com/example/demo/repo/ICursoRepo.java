package com.example.demo.repo;

import com.example.demo.model.Curso;
import reactor.core.publisher.Mono;

public interface ICursoRepo extends IGenericRepo<Curso,String>{
    Mono<Boolean> existsCursoByNombre(String nombre);
}
