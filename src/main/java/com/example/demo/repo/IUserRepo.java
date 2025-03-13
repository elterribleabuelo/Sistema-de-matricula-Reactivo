package com.example.demo.repo;

import com.example.demo.model.User;
import reactor.core.publisher.Mono;

public interface IUserRepo extends IGenericRepo<User,String>{
    Mono<User> findOneByUsername(String username);
}
