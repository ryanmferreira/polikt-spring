package com.polikt.api.guide.step;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface GuideStepRepository extends JpaRepository<GuideStep, Long> {
    List<GuideStep> findByGuideIdOrderByPositionAsc(Long guideId);
}