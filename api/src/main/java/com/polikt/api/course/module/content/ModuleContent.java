package com.polikt.api.course.module.content;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.polikt.api.course.module.Module;

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
@Table(name = "module_content")
public class ModuleContent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private int position;

    @Column
    private String content;

    @JoinColumn(name = "module_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
    private Module module;

    public ModuleContent() {
    }

    public ModuleContent(int position, String content, Module module) {
        this.position = position;
        this.content = content;
        this.module = module;
    }

    public Long getId() {
        return id;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Module getModule() {
        return module;
    }

    public void setModule(Module module) {
        this.module = module;
    }
}
