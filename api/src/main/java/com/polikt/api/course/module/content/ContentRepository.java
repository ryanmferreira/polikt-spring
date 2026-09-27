package com.polikt.api.course.module.content;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ContentRepository extends JpaRepository<Content, Long> {
    List<Content> findByModuleIdOrderByPositionAsc(Long moduleId);
}