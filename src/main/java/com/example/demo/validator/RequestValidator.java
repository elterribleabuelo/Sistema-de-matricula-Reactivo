package com.example.demo.validator;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor

public class RequestValidator {

    private final Validator validator;

    public<T> Mono<T> validate(T t){
        if(t==null){
            System.out.println("t es null");
            return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST));
        }

        System.out.println("t no es null");

        Set<ConstraintViolation<T>> constraints = validator.validate(t);

        if(constraints == null || constraints.isEmpty()){
            System.out.println("null o vacio" + constraints);
            return Mono.just(t);
        }else{

            String errores = constraints.stream()
                    .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                    .collect(Collectors.joining(", "));

            System.out.println("errores:" + errores);

            return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST,errores));

        }
    }
}
