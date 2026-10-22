package com.polikt.api.user;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.polikt.api.config.JwtService;

@RestController
@RequestMapping("/users")
public class UserController {

    // Inject the password encoder
    private final PasswordEncoder passwordEncoder;

    // Inject the repository
    private final UserRepository repository;

    // Inject the JWT Service
    private final JwtService jwtService;

    public UserController(UserRepository repository, JwtService jwtService, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
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
    public ResponseEntity<?> deleteUserById(@PathVariable Long id, Authentication authentication) {
        User user = repository.findById(id).orElse(null);

        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        if (!user.getEmail().equalsIgnoreCase(authentication.getName())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("message", "Access denied."));
        }

        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // POST /users
    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody User user) {
        if (user.getName() == null || user.getName().isBlank()
                || user.getEmail() == null || user.getEmail().isBlank()
                || user.getPassword() == null || user.getPassword().isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Name, email and password are required."));
        }

        if (repository.findByEmailIgnoreCase(user.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message", "Email already registered."));
        }

        if (user.getPhone() != null && repository.findByPhone(user.getPhone()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message", "Phone already registered."));
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(User.Role.ROLE_USER);

        return ResponseEntity.status(HttpStatus.CREATED).body(repository.save(user));
    }

    // PATCH /users/{id}
    @PatchMapping("/{id}")
    public ResponseEntity<?> updateUserById(@PathVariable Long id, @RequestBody User updatedUser,
            Authentication authentication) {
        User user = repository.findById(id).orElse(null);

        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        if (!user.getEmail().equalsIgnoreCase(authentication.getName())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("message", "Access denied."));
        }

        if (updatedUser.getName() != null) {
            user.setName(updatedUser.getName());
        }

        if (updatedUser.getEmail() != null) {
            User existingUser = repository.findByEmailIgnoreCase(updatedUser.getEmail()).orElse(null);

            if (existingUser != null && !existingUser.getId().equals(user.getId())) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message", "Email already registered."));
            }

            user.setEmail(updatedUser.getEmail());
        }

        if (updatedUser.getPhone() != null) {
            User existingUser = repository.findByPhone(updatedUser.getPhone()).orElse(null);

            if (existingUser != null && !existingUser.getId().equals(user.getId())) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message", "Phone already registered."));
            }

            user.setPhone(updatedUser.getPhone());
        }

        if (updatedUser.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        }

        User savedUser = repository.save(user);

        return ResponseEntity.ok(savedUser);
    }

    // POST /users/auth
    @PostMapping("/auth")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        User user = request.email() == null ? null : repository.findByEmailIgnoreCase(request.email()).orElse(null);

        if (user == null || request.password() == null
                || !passwordEncoder.matches(request.password(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Invalid email or password."));
        }

        String token = jwtService.generateToken(user.getEmail());
        return ResponseEntity.ok(new LoginResponse(token));
    }

    // GET /users/me
    @GetMapping("/me")
    public ResponseEntity<User> getCurrentUser(Authentication authentication) {
        String email = authentication.getName();

        User user = repository.findByEmailIgnoreCase(email).orElse(null);

        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(user);
    }

    // PATCH /users/me
    @PatchMapping("/me")
    public ResponseEntity<?> updateCurrentUser(@RequestBody User updatedUser, Authentication authentication) {
        String email = authentication.getName();

        User user = repository.findByEmailIgnoreCase(email).orElse(null);

        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        if (updatedUser.getName() != null) {
            user.setName(updatedUser.getName());
        }

        if (updatedUser.getEmail() != null) {
            User existingUser = repository.findByEmailIgnoreCase(updatedUser.getEmail()).orElse(null);

            if (existingUser != null && !existingUser.getId().equals(user.getId())) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message", "Email already registered."));
            }

            user.setEmail(updatedUser.getEmail());
        }

        if (updatedUser.getPhone() != null) {
            User existingUser = repository.findByPhone(updatedUser.getPhone()).orElse(null);

            if (existingUser != null && !existingUser.getId().equals(user.getId())) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message", "Phone already registered."));
            }

            user.setPhone(updatedUser.getPhone());
        }

        if (updatedUser.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        }

        User savedUser = repository.save(user);

        return ResponseEntity.ok(savedUser);
    }
}