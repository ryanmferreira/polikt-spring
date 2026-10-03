package com.polikt.api.course;

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
@RequestMapping("/courses")
public class CourseController {

    // Inject the repository
    private final CourseRepository repository;
    private final UserRepository userRepository;

    public CourseController(CourseRepository repository, UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    // GET /courses
    @GetMapping
    public List<Course> getAllCourses() {
        return repository.findAll();
    }

    // GET /courses/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Course> getCourseById(@PathVariable Long id) {
        Course course = repository.findById(id).orElse(null);

        if (course != null) {
            return ResponseEntity.ok(course);
        }

        return ResponseEntity.notFound().build();
    }

    // POST /courses
    @PostMapping
    public Course createCourse(@RequestBody Course course, Authentication authentication) {
        String email = authentication.getName();

        User author = userRepository.findByEmailIgnoreCase(email).orElse(null);

        course.setUser(author);

        return repository.save(course);
    }

    // DELETE /courses/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourseById(@PathVariable Long id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // PATCH /courses/{id}
    @PatchMapping("/{id}")
    public ResponseEntity<Course> updateCourseById(@PathVariable Long id, @RequestBody Course updatedCourse) {
        Course course = repository.findById(id).orElse(null);

        if (course == null) {
            return ResponseEntity.notFound().build();
        }

        if (updatedCourse.getTitle() != null) {
            course.setTitle(updatedCourse.getTitle());
        }

        if (updatedCourse.getDescription() != null) {
            course.setDescription(updatedCourse.getDescription());
        }

        if (updatedCourse.getCoverImage() != null) {
            course.setCoverImage(updatedCourse.getCoverImage());
        }

        Course savedCourse = repository.save(course);

        return ResponseEntity.ok(savedCourse);
    }
}
