package com.polikt.api.news;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@RestController
@RequestMapping("/news")
public class NewsController {

    // Inject the repository
    private final NewsRepository repository;

    // Constructor
    public NewsController(NewsRepository repository) {
        this.repository = repository;
    }

    // GET /news
    @GetMapping
    public List<News> getAllNews() {
        return repository.findAll();
    }

    // GET /news/{id}
    @GetMapping("/{id}")
    public ResponseEntity<News> getNewsById(@PathVariable int id) {
        News news = repository.findById(id).orElse(null);

        if (news != null) {
            return ResponseEntity.ok(news);
        }

        return ResponseEntity.notFound().build();
    }

    // POST /news
    @PostMapping
    public News createNews(@RequestBody News news) {
        return repository.save(news);
    }

    // DELETE /news/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNewsById(@PathVariable int id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // PUT /news/{id}
}
