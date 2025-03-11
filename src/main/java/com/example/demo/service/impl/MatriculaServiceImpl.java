package com.example.demo.service.impl;

import com.example.demo.model.Curso;
import com.example.demo.model.Matricula;
import com.example.demo.repo.IGenericRepo;
import com.example.demo.repo.IMatriculaRepo;
import com.example.demo.service.IMatriculaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;

@Service
@RequiredArgsConstructor

public class MatriculaServiceImpl extends CRUDImpl<Matricula,String> implements IMatriculaService {

    private final IMatriculaRepo repo;

    @Override
    protected IGenericRepo getRepo() {
        return repo;
    }

}
