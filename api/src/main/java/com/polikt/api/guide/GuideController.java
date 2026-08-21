package com.polikt.api.guide;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@RestController
@RequestMapping("/guides")
public class GuideController {

    private final GuideRepository repository;

    public GuideController(GuideRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Guide> getAllGuides() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Guide> getGuideById(@PathVariable int id) {
        Guide guide = repository.findById(id).orElse(null);

        if (guide != null) {
            return ResponseEntity.ok(guide);
        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public Guide createGuide(@RequestBody Guide guide) {
        return repository.save(guide);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGuideById(@PathVariable int id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}