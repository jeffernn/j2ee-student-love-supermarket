package org.example.xmut2.lovegoods.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "record")
@Getter
@Setter
public class TakeRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "item_id", nullable = false)
    private Long itemId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "type", nullable = false, length = 10)
    private String type; // STORE / TAKE

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    // toString方法，便于调试
    @Override
    public String toString() {
        return "TakeRecord{" +
                "id=" + id +
                ", itemId=" + itemId +
                ", userId=" + userId +
                ", type='" + type + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public Long getItemId() {
        return itemId;
    }
}
