package com.polikt.api.news;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@RestController
@RequestMapping("/news")
public class NewsController {

    private final NewsRepository repository;

    public NewsController(NewsRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<News> getAllNews() {
        return repository.findAll();
    }

    @PostMapping
    public News createNews(@RequestBody News news) {
        return repository.save(news);
    }

}
