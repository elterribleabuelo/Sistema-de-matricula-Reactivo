package com.example.demo.repo;

import com.example.demo.model.Curso;
import com.example.demo.model.Estudiante;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IEstudianteRepo extends IGenericRepo<Estudiante,String> {
    Mono<Boolean> existsEstudianteByDni(String dni);
    Flux<Estudiante> findAllByOrderByEdadAsc();
    Flux<Estudiante> findAllByOrderByEdadDesc();
}
