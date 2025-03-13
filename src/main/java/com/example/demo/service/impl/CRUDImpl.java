package com.example.demo.service.impl;

import com.example.demo.repo.IGenericRepo;
import com.example.demo.service.ICRUD;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public abstract class CRUDImpl<T,ID> implements ICRUD<T,ID> {

    protected abstract IGenericRepo<T,ID> getRepo();

    @Override
    public Mono<T> save(T t) {
        return getRepo().save(t);
    }

    @Override
    public Mono<T> update(ID id, T t) {

        Class<?> clazz = t.getClass();
        String className = clazz.getSimpleName();

        return getRepo().findById(id)
                .onErrorMap(Exception.class, e -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error en la BD", e))
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, className + " con ID : " + id + " no encontrado")))
                .flatMap(e->getRepo().save(t));
    }

    @Override
    public Flux<T> findAll() {
        return getRepo().findAll();
    }

    @Override
    public Mono<T> findById(ID id) {
        return getRepo().findById(id)
                .onErrorMap(Exception.class, e -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error en la BD", e))
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "no se encontró registros con ID:" + id)));
    }

    @Override
    public Mono<Boolean> delete(ID id) {
        return getRepo().findById(id)
                .onErrorMap(Exception.class, e -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error en la BD", e))
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "no se encontró el registro a eliminar")))
                .hasElement()
                .flatMap(result ->{
                    if(result) {
                        return getRepo().deleteById(id).thenReturn(true);
                    }else {
                        return Mono.just(false);
                    }
                });
    }
}
