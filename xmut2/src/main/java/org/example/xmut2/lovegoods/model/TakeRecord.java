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
}
