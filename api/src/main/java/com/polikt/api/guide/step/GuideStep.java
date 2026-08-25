package com.polikt.api.guide.step;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.polikt.api.guide.Guide;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "guide_steps")
public class GuideStep {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int position;

    @Column
    private String image;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @JoinColumn(name = "guide_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
    private Guide guide;

    public GuideStep() {
    }

    public GuideStep(int position, String image, String content, Guide guide) {
        this.position = position;
        this.image = image;
        this.content = content;
        this.guide = guide;
    }

    public Long getId() {
        return this.id;
    }

    public int getPosition() {
        return this.position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getImage() {
        return this.image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Guide getGuide() {
        return this.guide;
    }

    public void setGuide(Guide guide) {
        this.guide = guide;
    }
}