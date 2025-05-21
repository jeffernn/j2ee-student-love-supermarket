package org.example.xmut2.lovegoods.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.sql.Timestamp;


@Getter
@Setter
@Entity
@Table(name = "item")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "BIGINT") // 确保数据类型为 BIGINT
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "category", nullable = false, length = 50)
    private String category;

    @Column(name = "status", nullable = false, length = 10)
    private String status;

    @Column(name = "qrcode", nullable = false, length = 200)
    private String qrCode;

    @Column(name = "storage_location", length = 50)
    private String storageLocation;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;


    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    @Column(name = "is_stored", nullable = false)
    private boolean isStored;




    // 新增 isStored 的 getter 和 setter 方法
    public boolean isStored() {
        return isStored;
    }

    public void setIsStored(boolean stored) {
        this.isStored = stored;
    }


}