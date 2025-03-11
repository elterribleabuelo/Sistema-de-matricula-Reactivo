package com.example.demo.service.impl;

import com.example.demo.model.Estudiante;
import com.example.demo.repo.IEstudianteRepo;
import com.example.demo.repo.IGenericRepo;
import com.example.demo.service.IEstudianteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor

public class EstudianteServiceImpl extends CRUDImpl<Estudiante,String> implements IEstudianteService {

    private final IEstudianteRepo repo;

    @Override
    protected IGenericRepo getRepo() {
        return repo;
    }

    @Override
    public Mono<Boolean> existsEstudianteByDni(String dni) {
        return repo.existsEstudianteByDni(dni);
    }

    @Override
    public Flux<Estudiante> findAllByOrderByEdadAsc() {
        return repo.findAllByOrderByEdadAsc();
    }

    @Override
    public Flux<Estudiante> findAllByOrderByEdadDesc() {
        return repo.findAllByOrderByEdadDesc();
    }
}
