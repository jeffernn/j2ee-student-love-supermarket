package org.example.xmut2.lovegoods.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * 物品类别实体类
 * 用于存储物品的分类信息
 * 使用JPA注解进行对象关系映射
 */
@Data
@Getter
@Setter
@Entity
@Table(name = "category")
public class Category {

    /**
     * 类别ID
     * 主键，使用自增策略
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 类别名称
     * 非空且唯一
     */
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    /**
     * 获取类别ID
     * @return 类别ID
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置类别ID
     * @param id 类别ID
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取类别名称
     * @return 类别名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置类别名称
     * @param name 类别名称
     */
    public void setName(String name) {
        this.name = name;
    }
}
