package com.remote.restservice.delivery.note;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TestNoteRepository {

    private final JdbcTemplate jdbc;

    private final RowMapper<TestNote> mapper = (ResultSet rs, int rowNum) -> {
        TestNote note = new TestNote();
        note.setId(rs.getLong("Id"));
        note.setTitle(rs.getString("Title"));
        note.setContent(rs.getString("Content"));
        note.setStatus(rs.getString("Status"));
        note.setCategory(rs.getString("Category"));
        note.setCreatedAt(rs.getTimestamp("CreatedAt") != null ? rs.getTimestamp("CreatedAt").toLocalDateTime() : null);
        note.setUpdatedAt(rs.getTimestamp("UpdatedAt") != null ? rs.getTimestamp("UpdatedAt").toLocalDateTime() : null);
        note.setUserId(rs.getObject("UserId", Long.class)); // nullable
        note.setPhoneNumber(rs.getString("PhoneNumber"));   // nullable
        return note;
    };

    public List<TestNote> findAll() {
        return jdbc.query("SELECT * FROM test_Notes", mapper);
    }

    public Optional<TestNote> findById(Long id) {
        List<TestNote> result = jdbc.query("SELECT * FROM test_Notes WHERE Id = ?", mapper, id);
        return result.stream().findFirst();
    }

    public int insert(TestNote note) {
        return jdbc.update("""
            INSERT INTO test_Notes (Title, Content, Status, Category, CreatedAt, UpdatedAt, UserId, PhoneNumber)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
        """,
                note.getTitle(),
                note.getContent(),
                note.getStatus(),
                note.getCategory(),
                Timestamp.valueOf(note.getCreatedAt()),
                Timestamp.valueOf(note.getUpdatedAt()),
                note.getUserId(),
                note.getPhoneNumber());
    }

    public int update(TestNote note) {
        return jdbc.update("""
            UPDATE test_Notes SET Title = ?, Content = ?, Status = ?, Category = ?, UpdatedAt = ?, UserId = ?, PhoneNumber = ?
            WHERE Id = ?
        """,
                note.getTitle(),
                note.getContent(),
                note.getStatus(),
                note.getCategory(),
                Timestamp.valueOf(note.getUpdatedAt()),
                note.getUserId(),
                note.getPhoneNumber(),
                note.getId());
    }

    public int delete(Long id) {
        return jdbc.update("DELETE FROM test_Notes WHERE Id = ?", id);
    }
}
