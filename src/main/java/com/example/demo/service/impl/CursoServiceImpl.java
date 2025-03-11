package com.example.demo.service.impl;

import com.example.demo.dto.CursoDTO;
import com.example.demo.model.Curso;
import com.example.demo.repo.ICursoRepo;
import com.example.demo.repo.IGenericRepo;
import com.example.demo.service.ICursoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CursoServiceImpl extends CRUDImpl<Curso,String> implements ICursoService {

    private final ICursoRepo repo;

    @Override
    protected IGenericRepo getRepo() {
        return repo;
    }

    @Override
    public Mono<Boolean> existsCursoByNombre(String nombre) {
        return repo.existsCursoByNombre(nombre);
    }

    @Override
    public Flux<Curso> findAllById(List<String> ids) {
        Flux<String> ids_flux = Flux.fromIterable(ids);
        return repo.findAllById(ids_flux);
    }

}
