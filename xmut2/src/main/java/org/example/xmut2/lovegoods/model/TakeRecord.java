package org.example.xmut2.lovegoods.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 取物记录实体类
 * 用于记录物品的存取操作
 * 包括存储和取出两种类型
 */
@Entity
@Table(name = "record")
@Getter
@Setter
public class TakeRecord {

    /**
     * 记录ID
     * 主键，使用自增策略
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 物品ID
     * 非空，关联到物品表
     */
    @Column(name = "item_id", nullable = false)
    private Long itemId;

    /**
     * 用户ID
     * 非空，关联到用户表
     */
    @Column(name = "user_id", nullable = false)
    private Long userId;

    /**
     * 操作类型
     * 非空，最大长度10
     * 可选值：STORE（存储）/ TAKE（取出）
     */
    @Column(name = "type", nullable = false, length = 10)
    private String type;

    /**
     * 创建时间
     * 非空，记录操作发生的时间
     */
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    /**
     * 重写toString方法
     * 用于调试和日志记录
     * @return 记录信息的字符串表示
     */
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

    /**
     * 设置物品ID
     * @param itemId 物品ID
     */
    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    /**
     * 设置用户ID
     * @param userId 用户ID
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * 设置操作类型
     * @param type 操作类型（STORE/TAKE）
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * 设置创建时间
     * @param createdAt 创建时间
     */
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * 获取创建时间
     * @return 创建时间
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * 获取物品ID
     * @return 物品ID
     */
    public Long getItemId() {
        return itemId;
    }

    /**
     * 获取记录ID
     * @return 记录ID
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置记录ID
     * @param id 记录ID
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取用户ID
     * @return 用户ID
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * 获取操作类型
     * @return 操作类型
     */
    public String getType() {
        return type;
    }
}
