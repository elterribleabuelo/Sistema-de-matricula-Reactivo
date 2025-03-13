package com.example.demo.service;

import com.example.demo.model.User;
import reactor.core.publisher.Mono;

public interface IUserService {
    Mono<User> saveHash(User user);
    Mono<com.example.demo.security.User> searchByUser(String username);
}
