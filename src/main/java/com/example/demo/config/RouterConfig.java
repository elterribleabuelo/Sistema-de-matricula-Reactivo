package com.example.demo.config;

import com.example.demo.handler.CursoHandler;
import com.example.demo.handler.EstudianteHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterConfig {

    @Bean
    public RouterFunction<ServerResponse> cursosRoutes(CursoHandler handler){
        return route(GET("/v2/cursos"),handler::findAll)
                .andRoute(GET("/v2/cursos/{id}"),handler::findById)
                .andRoute(POST("/v2/cursos"),handler::save)
                .andRoute(PUT("/v2/cursos/{id}"),handler::update)
                .andRoute(DELETE("/v2/cursos/{id}"),handler::delete);
    }

    @Bean
    public RouterFunction<ServerResponse> estudiantesRoutes(EstudianteHandler handler){
        return   route(GET("/v2/estudiantes"),handler::findAll)
                .andRoute(GET("/v2/estudiantes/ordenadoAscPorEdad"),handler::findAllByOrderByEdadAsc)
                .andRoute(GET("/v2/estudiantes/ordenadoDescPorEdad"),handler::findAllByOrderByEdadDesc)
                .andRoute(GET("/v2/estudiantes/{id}"),handler::findById)
                .andRoute(POST("/v2/estudiantes"),handler::save)
                .andRoute(PUT("/v2/estudiantes/{id}"),handler::update)
                .andRoute(DELETE("/v2/estudiantes/{id}"),handler::delete);
    }

}
