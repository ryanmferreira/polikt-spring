package com.polikt.api.course.module;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.polikt.api.course.Course;
import com.polikt.api.course.CourseRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/courses/{courseId}/modules")
public class ModuleController {

    // Inject the module repository
    private final ModuleRepository repository;

    // Inject the course repository
    private final CourseRepository courseRepository;

    public ModuleController(ModuleRepository repository, CourseRepository courseRepository) {
        this.repository = repository;
        this.courseRepository = courseRepository;
    }

    // GET /modules
    @GetMapping
    public List<Module> getAllModules(@PathVariable Long courseId) {
        return repository.findByCourseIdOrderByPositionAsc(courseId);
    }

    // GET /modules/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Module> getModuleById(@PathVariable Long courseId, @PathVariable Long id) {
        Module module = repository.findById(id).orElse(null);

        if (module != null && module.getCourse().getId().equals(courseId)) {
            return ResponseEntity.ok(module);
        }

        return ResponseEntity.notFound().build();
    }

    // POST /modules
    @PostMapping
    public ResponseEntity<Module> createModule(@PathVariable Long courseId, @RequestBody Module module) {
        Course course = courseRepository.findById(courseId).orElse(null);

        if (course == null) {
            return ResponseEntity.notFound().build();
        }

        module.setCourse(course);

        return ResponseEntity.ok(repository.save(module));
    }

    // DELETE /modules/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteModuleById(@PathVariable Long courseId, @PathVariable Long id) {
        Module module = repository.findById(id).orElse(null);

        if (module == null || !module.getCourse().getId().equals(courseId)) {
            return ResponseEntity.notFound().build();
        }

        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // PATCH /modules/{id}
    @PatchMapping("/{id}")
    public ResponseEntity<Module> updateCourseById(@PathVariable Long id, @RequestBody Module updatedModule) {
        Module module = repository.findById(id).orElse(null);

        if (module == null) {
            return ResponseEntity.notFound().build();
        }

        if (updatedModule.getTitle() != null) {
            module.setTitle(updatedModule.getTitle());
        }

        if (updatedModule.getCoverImage() != null) {
            module.setCoverImage(updatedModule.getCoverImage());
        }

        if (updatedModule.getDescription() != null) {
            module.setDescription(updatedModule.getDescription());
        }

        if (updatedModule.getPosition() != null) {
            module.setPosition(updatedModule.getPosition());
        }

        Module savedModule = repository.save(module);

        return ResponseEntity.ok(savedModule);
    }
}