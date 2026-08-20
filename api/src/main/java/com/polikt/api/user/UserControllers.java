package com.polikt.api.user;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserControllers {

    private final UserRepository repository;

    public UserControllers(UserRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<UserEntity> getAllUsers() {
        return repository.findAll();
    }

    @PostMapping
    public UserEntity createUser(@RequestBody UserEntity user) {
        return repository.save(user);
    }
}