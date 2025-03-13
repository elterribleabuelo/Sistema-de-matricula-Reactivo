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
        return getRepo().findById(id)
                .onErrorMap(Exception.class, e -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error en la BD", e))
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "ID NOT FOUND" + id )))
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
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "ID NOT FOUND:" + id)));
    }

    @Override
    public Mono<Boolean> delete(ID id) {
        return getRepo().findById(id)
                .onErrorMap(Exception.class, e -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error en la BD", e))
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "ID NOT FOUND:" + id)))
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
