package com.polikt.api.course.module.content;

import java.util.List;

import org.springframework.http.HttpStatus;
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

    // Inject the Content repository
    private final ContentRepository repository;

    // Inject the Module repository
    private final ModuleRepository moduleRepository;

    public ContentController(ContentRepository repository, ModuleRepository moduleRepository) {
        this.repository = repository;
        this.moduleRepository = moduleRepository;
    }

    // GET /courses/{courseId}/modules/{moduleId}/content
    @GetMapping
    public ResponseEntity<List<Content>> getAllModuleContents(
            @PathVariable Long courseId,
            @PathVariable Long moduleId) {

        if (findModuleInCourse(courseId, moduleId) == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(repository.findByModuleIdOrderByPositionAsc(moduleId));
    }

    // GET /courses/{courseId}/modules/{moduleId}/content/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Content> getModuleContentById(
            @PathVariable Long courseId,
            @PathVariable Long moduleId,
            @PathVariable Long id) {

        Content content = findContentInModule(courseId, moduleId, id);

        if (content == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(content);
    }

    // POST /courses/{courseId}/modules/{moduleId}/content
    @PostMapping
    public ResponseEntity<Content> createModule(
            @PathVariable Long courseId,
            @PathVariable Long moduleId,
            @RequestBody Content content) {

        Module module = findModuleInCourse(courseId, moduleId);

        if (module == null) {
            return ResponseEntity.notFound().build();
        }

        content.setModule(module);

        return ResponseEntity.status(HttpStatus.CREATED).body(repository.save(content));
    }

    // DELETE /courses/{courseId}/modules/{moduleId}/content/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteModuleContentById(
            @PathVariable Long courseId,
            @PathVariable Long moduleId,
            @PathVariable Long id) {

        Content content = findContentInModule(courseId, moduleId, id);

        if (content == null) {
            return ResponseEntity.notFound().build();
        }

        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // PATCH /courses/{courseId}/modules/{moduleId}/content/{id}
    @PatchMapping("/{id}")
    public ResponseEntity<Content> updateContentById(
            @PathVariable Long courseId,
            @PathVariable Long moduleId,
            @PathVariable Long id, @RequestBody Content updatedContent) {

        Content content = findContentInModule(courseId, moduleId, id);

        if (content == null) {
            return ResponseEntity.notFound().build();
        }

        if (updatedContent.getContent() != null) {
            content.setContent(updatedContent.getContent());
        }

        if (updatedContent.getCoverImage() != null) {
            content.setCoverImage(updatedContent.getCoverImage());
        }

        if (updatedContent.getPosition() != null) {
            content.setPosition(updatedContent.getPosition());
        }

        Content savedContent = repository.save(content);

        return ResponseEntity.ok(savedContent);
    }

    // Finds a module and confirms it belongs to the course
    private Module findModuleInCourse(Long courseId, Long moduleId) {
        Module module = moduleRepository.findById(moduleId).orElse(null);

        if (module == null || !module.getCourse().getId().equals(courseId)) {
            return null;
        }

        return module;
    }

    // Finds a content and confirms it belongs to the module (and the module to the
    // course)
    private Content findContentInModule(Long courseId, Long moduleId, Long id) {
        Content content = repository.findById(id).orElse(null);

        if (content == null || findModuleInCourse(courseId, moduleId) == null) {
            return null;
        }

        if (!content.getModule().getId().equals(moduleId)) {
            return null;
        }

        return content;
    }
}
