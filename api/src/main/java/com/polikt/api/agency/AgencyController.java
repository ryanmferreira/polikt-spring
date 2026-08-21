package com.polikt.api.agency;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/agencies")
public class AgencyController {

    private final AgencyRepository repository;

    public AgencyController(AgencyRepository repository) {
        this.repository = repository;
    }

    // GET /agencies
    @GetMapping
    public List<Agency> getAllAgencies() {
        return repository.findAll();
    }

    // GET /agencies/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Agency> getAgencyById(@PathVariable int id) {
        Agency agency = repository.findById(id).orElse(null);

        if (agency != null) {
            return ResponseEntity.ok(agency);
        }

        return ResponseEntity.notFound().build();
    }

    // POST /agencies
    @PostMapping
    public Agency createAgency(@RequestBody Agency agency) {
        return repository.save(agency);
    }

    // DELETE /agencies/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAgencyById(@PathVariable int id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
