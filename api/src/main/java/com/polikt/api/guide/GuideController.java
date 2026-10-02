package com.polikt.api.guide;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.polikt.api.user.User;
import com.polikt.api.user.UserRepository;

@RestController
@RequestMapping("/guides")
public class GuideController {

    // Inject the repository
    private final GuideRepository repository;

    // Inject the user repository
    private final UserRepository userRepository;

    public GuideController(GuideRepository repository, UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    // GET /guides
    @GetMapping
    public List<Guide> getAllGuides() {
        return repository.findAll();
    }

    // GET /guides/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Guide> getGuideById(@PathVariable Long id) {
        Guide guide = repository.findById(id).orElse(null);

        if (guide != null) {
            return ResponseEntity.ok(guide);
        }

        return ResponseEntity.notFound().build();
    }

    // POST /guides
    @PostMapping
    public Guide createGuide(@RequestBody Guide guide, Authentication authentication) {
        String email = authentication.getName();

        User author = userRepository.findByEmail(email).orElse(null);

        guide.setUser(author);

        return repository.save(guide);
    }

    // DELETE /guides/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGuideById(@PathVariable Long id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // PATCH /guides/{id}
    @PatchMapping("/{id}")
    public ResponseEntity<Guide> updateGuideById(@PathVariable Long id, @RequestBody Guide updatedGuide) {
        Guide guide = repository.findById(id).orElse(null);

        if (guide == null) {
            return ResponseEntity.notFound().build();
        }

        if (updatedGuide.getTitle() != null) {
            guide.setTitle(updatedGuide.getTitle());
        }

        if (updatedGuide.getDescription() != null) {
            guide.setDescription(updatedGuide.getDescription());
        }

        if (updatedGuide.getCoverImage() != null) {
            guide.setCoverImage(updatedGuide.getCoverImage());
        }

        Guide savedGuide = repository.save(guide);

        return ResponseEntity.ok(savedGuide);
    }
}