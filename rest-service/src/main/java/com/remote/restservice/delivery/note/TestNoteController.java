package com.remote.restservice.delivery.note;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notes")
@RequiredArgsConstructor
public class TestNoteController {

    private final TestNoteService service;

    @GetMapping
    public ResponseEntity<List<TestNote>> getAll() {
        return ResponseEntity.ok(service.getAllNotes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TestNote> get(@PathVariable Long id) {
        return service.getNote(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<String> create(@RequestBody TestNote note) {
        if (service.createNote(note)) {
            return ResponseEntity.ok("Created");
        } else {
            return ResponseEntity.status(500).body("Failed to insert");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable Long id, @RequestBody TestNote note) {
        note.setId(id);
        if (service.updateNote(note)) {
            return ResponseEntity.ok("Updated");
        } else {
            return ResponseEntity.status(404).body("Not Found");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        if (service.deleteNote(id)) {
            return ResponseEntity.ok("Deleted");
        } else {
            return ResponseEntity.status(404).body("Not Found");
        }
    }
}
