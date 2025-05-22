package com.remote.restservice.delivery.note;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "test_notes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TestNote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(columnDefinition = "NVARCHAR(MAX)")
    private String content;

    private String status;

    private String category;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @Column(nullable = true)
    private Long userId; // ✅ 사용자 ID (nullable)

    @Column(nullable = true, length = 20)
    private String phoneNumber; // ✅ 전화번호 (nullable)

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
