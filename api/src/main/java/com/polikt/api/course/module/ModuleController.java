package com.polikt.api.course.module;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
    private final ModuleRepository repository;
    private final CourseRepository courseRepository;

    public ModuleController(ModuleRepository repository, CourseRepository courseRepository) {
        this.repository = repository;
        this.courseRepository = courseRepository;
    }

    @GetMapping
    public List<Module> getAllModules(@PathVariable Long courseId) {
        return repository.findByCourseIdOrderByPositionAsc(courseId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Module> getModuleById(@PathVariable Long courseId, @PathVariable Long id) {
        Module module = repository.findById(id).orElse(null);

        if (module != null && module.getCourse().getId().equals(courseId)) {
            return ResponseEntity.ok(module);
        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Module> postMethodName(@PathVariable Long courseId, @RequestBody Module module) {
        Course course = courseRepository.findById(courseId).orElse(null);

        if (course == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(repository.save(module));
    }

}
