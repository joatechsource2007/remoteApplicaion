package com.remote.restservice.delivery.note;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "test_notes")
@Data // ✅ 모든 getter/setter, equals, hashCode, toString 포함
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

    // 생성 시 자동으로 createdAt 설정
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    // 수정 시 자동으로 updatedAt 설정
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
