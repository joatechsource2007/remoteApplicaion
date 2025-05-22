package com.remote.restservice.delivery.note;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TestNoteService {

    private final TestNoteRepository repo;

    public List<TestNote> getAllNotes() {
        return repo.findAll();
    }

    public Optional<TestNote> getNote(Long id) {
        return repo.findById(id);
    }

    public boolean createNote(TestNote note) {
        LocalDateTime now = LocalDateTime.now();
        note.setCreatedAt(now);
        note.setUpdatedAt(now);
        return repo.insert(note) > 0;
    }

    public boolean updateNote(TestNote note) {
        note.setUpdatedAt(LocalDateTime.now());
        return repo.update(note) > 0;
    }

    public boolean deleteNote(Long id) {
        return repo.delete(id) > 0;
    }
}
