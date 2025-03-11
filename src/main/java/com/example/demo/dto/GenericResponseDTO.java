package com.example.demo.dto;

public record GenericResponseDTO(
        int status,
        String message,
        Object data
) {
}
