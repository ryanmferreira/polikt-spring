package com.polikt.api.guide.step;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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

    private final GuideStepRepository repository;
    private final GuideRepository guideRepository;

    public GuideStepController(GuideStepRepository repository, GuideRepository guideRepository) {
        this.repository = repository;
        this.guideRepository = guideRepository;
    }

    @GetMapping
    public List<GuideStep> getAllSteps(@PathVariable Long guideId) {
        return repository.findByGuideIdOrderByPositionAsc(guideId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GuideStep> getStepById(@PathVariable Long guideId, @PathVariable Long id) {
        GuideStep step = repository.findById(id).orElse(null);

        if (step != null && step.getGuide().getId().equals(guideId)) {
            return ResponseEntity.ok(step);
        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<GuideStep> createStep(@PathVariable Long guideId, @RequestBody GuideStep step) {
        Guide guide = guideRepository.findById(guideId).orElse(null);

        if (guide == null) {
            return ResponseEntity.notFound().build();
        }

        step.setGuide(guide);
        return ResponseEntity.ok(repository.save(step));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStepById(@PathVariable Long guideId, @PathVariable Long id) {
        GuideStep step = repository.findById(id).orElse(null);

        if (step == null || !step.getGuide().getId().equals(guideId)) {
            return ResponseEntity.notFound().build();
        }

        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}