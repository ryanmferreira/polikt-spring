package com.polikt.api.guide.step;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
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

import com.polikt.api.guide.Guide;
import com.polikt.api.guide.GuideRepository;

@RestController
@RequestMapping("/guides/{guideId}/steps")
public class GuideStepController {

    // Inject the Guide Step repository
    private final GuideStepRepository repository;

    // Inject the Guide repository
    private final GuideRepository guideRepository;

    public GuideStepController(GuideStepRepository repository, GuideRepository guideRepository) {
        this.repository = repository;
        this.guideRepository = guideRepository;
    }

    // GET /guides/{guideId}/steps
    @GetMapping
    public List<GuideStep> getAllSteps(@PathVariable Long guideId) {
        return repository.findByGuideIdOrderByPositionAsc(guideId);
    }

    // GET /guides/{guideId}/steps/{id}
    @GetMapping("/{id}")
    public ResponseEntity<GuideStep> getStepById(@PathVariable Long guideId, @PathVariable Long id) {
        GuideStep step = repository.findById(id).orElse(null);

        if (step != null && step.getGuide().getId().equals(guideId)) {
            return ResponseEntity.ok(step);
        }

        return ResponseEntity.notFound().build();
    }

    // POST /guides/{guideId}/steps
    @PostMapping
    public ResponseEntity<?> createStep(@PathVariable Long guideId, @RequestBody GuideStep step,
            Authentication authentication) {
        Guide guide = guideRepository.findById(guideId).orElse(null);

        if (guide == null) {
            return ResponseEntity.notFound().build();
        }

        if (!guide.getUser().getEmail().equalsIgnoreCase(authentication.getName())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("message", "Access denied."));
        }

        step.setGuide(guide);

        return ResponseEntity.status(HttpStatus.CREATED).body(repository.save(step));
    }

    // DELETE /guides/{guideId}/steps/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStepById(@PathVariable Long guideId, @PathVariable Long id,
            Authentication authentication) {
        GuideStep step = repository.findById(id).orElse(null);

        if (step == null || !step.getGuide().getId().equals(guideId)) {
            return ResponseEntity.notFound().build();
        }

        if (!step.getGuide().getUser().getEmail().equalsIgnoreCase(authentication.getName())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("message", "Access denied."));
        }

        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // PATCH /guides/{guideId}/steps/{id}
    @PatchMapping("/{id}")
    public ResponseEntity<?> updateCourseById(
            @PathVariable Long guideId,
            @PathVariable Long id,
            @RequestBody GuideStep updatedStep,
            Authentication authentication) {

        GuideStep step = repository.findById(id).orElse(null);

        if (step == null || !step.getGuide().getId().equals(guideId)) {
            return ResponseEntity.notFound().build();
        }

        if (!step.getGuide().getUser().getEmail().equalsIgnoreCase(authentication.getName())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("message", "Access denied."));
        }

        if (updatedStep.getPosition() != null) {
            step.setPosition(updatedStep.getPosition());
        }

        if (updatedStep.getImage() != null) {
            step.setImage(updatedStep.getImage());
        }

        if (updatedStep.getContent() != null) {
            step.setContent(updatedStep.getContent());
        }

        GuideStep savedStep = repository.save(step);

        return ResponseEntity.ok(savedStep);
    }
}