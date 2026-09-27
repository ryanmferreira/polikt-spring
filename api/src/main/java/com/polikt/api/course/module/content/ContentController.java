package com.polikt.api.course.module.content;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.polikt.api.course.module.Module;
import com.polikt.api.course.module.ModuleRepository;

@RestController
@RequestMapping("/courses/{courseId}/modules/{moduleId}/content")
public class ContentController {

    // Inject the Module repository
    private final ModuleRepository moduleRepository;

    // Inject the Content repository
    private final ContentRepository repository;

    public ContentController(ModuleRepository moduleRepository, ContentRepository repository) {
        this.moduleRepository = moduleRepository;
        this.repository = repository;
    }

    // GET /modules/{moduleId}/content
    @GetMapping
    public List<Content> getAllModuleContents(@PathVariable Long moduleId) {
        return repository.findByModuleIdOrderByPositionAsc(moduleId);
    }

    // GET /modules/{moduleId}/content/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Content> getModuleContentById(@PathVariable Long id) {
        Content content = repository.findById(id).orElse(null);

        if (content == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(content);
    }

    // POST /modules/{moduleId}/content
    @PostMapping
    public ResponseEntity<Content> createModule(@PathVariable Long moduleId, @RequestBody Content content) {
        Module module = moduleRepository.findById(moduleId).orElse(null);

        if (module == null) {
            return ResponseEntity.notFound().build();
        }

        content.setModule(module);

        return ResponseEntity.ok(repository.save(content));
    }

    // DELETE /modules/{moduleId}/content/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteModuleContentById(@PathVariable Long moduleId, @PathVariable Long id) {
        Content content = repository.findById(id).orElse(null);

        if (content == null || !content.getModule().getId().equals(moduleId)) {
            return ResponseEntity.notFound().build();
        }

        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // PATCH /modules/{moduleId}/content/{id}
    @PatchMapping("/{id}")
    public ResponseEntity<Content> updateContentById(@PathVariable Long moduleId, @PathVariable Long id) {
        Content content = repository.findById(id).orElse(null);

        if (content == null || !content.getModule().getId().equals(moduleId)) {
            return ResponseEntity.notFound().build();
        }

        if (content.getContent() != null) {
            content.setContent(content.getContent());
        }

        if (content.getCoverImage() != null) {
            content.setCoverImage(content.getCoverImage());
        }

        if (content.getPosition() != null) {
            content.setPosition(content.getPosition());
        }

        Content savedContent = repository.save(content);

        return ResponseEntity.ok(savedContent);
    }
}