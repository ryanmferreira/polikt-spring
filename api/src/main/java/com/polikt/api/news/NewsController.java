package com.polikt.api.news;

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
@RequestMapping("/news")
public class NewsController {

    // Inject the repository
    private final NewsRepository repository;

    // Inject the user repository
    private final UserRepository userRepository;

    // Constructor
    public NewsController(NewsRepository repository, UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    // GET /news
    @GetMapping
    public List<News> getAllNews() {
        return repository.findAll();
    }

    // GET /news/{id}
    @GetMapping("/{id}")
    public ResponseEntity<News> getNewsById(@PathVariable Long id) {
        News news = repository.findById(id).orElse(null);

        if (news != null) {
            return ResponseEntity.ok(news);
        }

        return ResponseEntity.notFound().build();
    }

    // POST /news
    @PostMapping
    public News createNews(@RequestBody News news, Authentication authentication) {
        String email = authentication.getName();

        User author = userRepository.findByEmail(email).orElse(null);

        news.setUser(author);

        return repository.save(news);
    }

    // DELETE /news/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNewsById(@PathVariable Long id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // PUT /news/{id}
    @PatchMapping("/{id}")
    public ResponseEntity<News> updateCourseById(@PathVariable Long id, @RequestBody News updatedNews) {
        News news = repository.findById(id).orElse(null);

        if (news == null) {
            return ResponseEntity.notFound().build();
        }

        if (updatedNews.getTitle() != null) {
            news.setTitle(updatedNews.getTitle());
        }

        if (updatedNews.getDescription() != null) {
            news.setDescription(updatedNews.getDescription());
        }

        if (updatedNews.getCoverImage() != null) {
            news.setCoverImage(updatedNews.getCoverImage());
        }

        if (updatedNews.getContent() != null) {
            news.setContent(updatedNews.getContent());
        }

        if (updatedNews.getSummary() != null) {
            news.setSummary(updatedNews.getSummary());
        }

        if (updatedNews.getBody() != null) {
            news.setBody(updatedNews.getBody());
        }

        News savedNews = repository.save(news);

        return ResponseEntity.ok(savedNews);
    }
}
