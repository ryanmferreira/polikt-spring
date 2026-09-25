package com.polikt.api.user;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.polikt.api.config.JwtService;

@RestController
@RequestMapping("/users")
public class UserController {

    // Inject the repository
    private final UserRepository repository;

    // Inject the JWT Service
    private final JwtService jwtService;

    public UserController(UserRepository repository, JwtService jwtService) {
        this.repository = repository;
        this.jwtService = jwtService;
    }

    // GET /users
    @GetMapping
    public List<User> getAllUsers() {
        return repository.findAll();
    }

    // GET /users/{id}
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = repository.findById(id).orElse(null);

        if (user != null) {
            return ResponseEntity.ok(user);
        }

        return ResponseEntity.notFound().build();
    }

    // Delete /users/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable Long id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // POST /users
    @PostMapping
    public User createUser(@RequestBody User user) {
        return repository.save(user);
    }

    // POST /users/login
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        User user = repository.findByEmail(request.email()).orElse(null);

        if (user == null || !user.getPassword().equals(request.password())) {
            return ResponseEntity.status(401).build();
        }

        String token = jwtService.generateToken(user.getEmail());
        return ResponseEntity.ok(new LoginResponse(token));
    }
}
