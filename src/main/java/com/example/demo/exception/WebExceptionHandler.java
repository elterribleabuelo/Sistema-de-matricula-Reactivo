package com.example.demo.exception;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@Component
@Order(-1)

public class WebExceptionHandler extends AbstractErrorWebExceptionHandler {

    public WebExceptionHandler(ErrorAttributes errorAtributes, WebProperties.Resources resources,
                               ApplicationContext applicationContext, ServerCodecConfigurer configurer) {
        super(errorAtributes, resources, applicationContext);
        setMessageWriters(configurer.getWriters());
    }

    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse);
    }

    private Mono<ServerResponse> renderErrorResponse(ServerRequest req) {

        Map<String, Object> generalError = getErrorAttributes(req, ErrorAttributeOptions.defaults());
        int statusCode = Integer.valueOf(String.valueOf(generalError.get("status").toString()));

        Throwable err = getError(req);

        if (err instanceof WebExchangeBindException webExchangeBindException) {

            return handleValidationException(webExchangeBindException,statusCode);

        }else if(err instanceof ResponseStatusException responseStatusException){

            statusCode = responseStatusException.getStatusCode().value();

            return ServerResponse
                    .status(statusCode)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(new CustomErrorResponse(LocalDateTime.now(),
                            responseStatusException.getReason(),
                            statusCode)
                    );
        }else{
            return handleGeneralException(err,statusCode);
        }
    }

    private Mono<ServerResponse> handleGeneralException(Throwable err,int statusCode) {

        if (err instanceof IllegalArgumentException) {
            return ServerResponse
                    .status(statusCode)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(new CustomErrorResponse(LocalDateTime.now(),"Parámetro inválido: " + err.getMessage(),statusCode));
        } else {
            return ServerResponse
                    .status(statusCode)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(new CustomErrorResponse(LocalDateTime.now(),err.getMessage(),statusCode));
        }
    }

    private Mono<ServerResponse> handleValidationException(WebExchangeBindException ex,int statusCode) {

        String errores = ex.getBindingResult().getFieldErrors().stream()
                                                            .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                                                            .collect(Collectors.joining(", "));


        return ServerResponse.status(statusCode)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new CustomErrorResponse(LocalDateTime.now(),errores,statusCode));

    }

}
