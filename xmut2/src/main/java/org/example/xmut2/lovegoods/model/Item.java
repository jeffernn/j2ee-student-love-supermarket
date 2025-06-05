package org.example.xmut2.lovegoods.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.sql.Timestamp;

/**
 * 物品实体类
 * 用于存储物品的基本信息和状态
 * 使用JPA注解进行对象关系映射
 */
@Getter
@Setter
@Entity
@Table(name = "item")
public class Item {
    /**
     * 物品ID
     * 主键，使用自增策略
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "BIGINT") // 确保数据类型为 BIGINT
    private Long id;

    /**
     * 物品名称
     * 非空，最大长度100
     */
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    /**
     * 物品类别
     * 非空，最大长度50
     */
    @Column(name = "category", nullable = false, length = 50)
    private String category;

    /**
     * 物品状态
     * 非空，最大长度10
     */
    @Column(name = "status", nullable = false, length = 10)
    private String status;

    /**
     * 物品二维码
     * 非空，最大长度200
     */
    @Column(name = "qrcode", nullable = false, length = 200)
    private String qrCode;

    /**
     * 存储位置
     * 最大长度50
     */
    @Column(name = "storage_location", length = 50)
    private String storageLocation;

    /**
     * 物品所有者
     * 多对一关系，关联到用户表
     */
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    /**
     * 创建时间
     * 不可更新
     */
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    /**
     * 更新时间
     * 非空
     */
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    /**
     * 是否已存储
     * 非空
     */
    @Column(name = "is_stored", nullable = false)
    private boolean isStored;

    /**
     * 获取存储状态
     * @return 是否已存储
     */
    public boolean getStored() {
        return isStored;
    }

    /**
     * 设置存储状态
     * @param stored 是否已存储
     */
    public void setStored(boolean stored) {
        this.isStored = stored;
    }

    /**
     * 获取物品名称
     * @return 物品名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置物品名称
     * @param name 物品名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取物品类别
     * @return 物品类别
     */
    public String getCategory() {
        return category;
    }

    /**
     * 设置物品类别
     * @param category 物品类别
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * 获取物品状态
     * @return 物品状态
     */
    public String getStatus() {
        return status;
    }

    /**
     * 设置物品状态
     * @param status 物品状态
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * 获取物品二维码
     * @return 物品二维码
     */
    public String getQrCode() {
        return qrCode;
    }

    /**
     * 设置物品二维码
     * @param qrCode 物品二维码
     */
    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    /**
     * 获取存储位置
     * @return 存储位置
     */
    public String getStorageLocation() {
        return storageLocation;
    }

    /**
     * 设置存储位置
     * @param storageLocation 存储位置
     */
    public void setStorageLocation(String storageLocation) {
        this.storageLocation = storageLocation;
    }

    /**
     * 获取物品所有者
     * @return 物品所有者
     */
    public User getUser() {
        return user;
    }

    /**
     * 设置物品所有者
     * @param user 物品所有者
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * 获取创建时间
     * @return 创建时间
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * 设置创建时间
     * @param createdAt 创建时间
     */
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * 获取更新时间
     * @return 更新时间
     */
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    /**
     * 设置更新时间
     * @param updatedAt 更新时间
     */
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    /**
     * 获取物品ID
     * @return 物品ID
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置物品ID
     * @param id 物品ID
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 检查物品是否已存储
     * @return 是否已存储
     */
    public boolean isStored() {
        return isStored;
    }

    /**
     * 设置物品存储状态
     * @param stored 是否已存储
     */
    public void setIsStored(boolean stored) {
        this.isStored = stored;
    }
}