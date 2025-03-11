package com.example.demo.service;

import com.example.demo.model.Estudiante;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface IEstudianteService extends ICRUD<Estudiante,String>{
    Mono<Boolean> existsEstudianteByDni(String dni);
    Flux<Estudiante> findAllByOrderByEdadAsc();
    Flux<Estudiante> findAllByOrderByEdadDesc();
}
