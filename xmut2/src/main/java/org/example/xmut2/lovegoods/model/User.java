package org.example.xmut2.lovegoods.model;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * 用户实体类
 * 实现UserDetails接口以支持Spring Security认证
 * 用于存储用户的基本信息和认证信息
 */
@Entity
@Table(name = "user")
public class User implements UserDetails {

    /**
     * 用户ID
     * 主键，使用自增策略
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "BIGINT")
    private Long id;

    /**
     * 用户姓名
     * 非空，最大长度50
     */
    @Column(name = "name", nullable = false, length = 50)
    private String name;

    /**
     * 用户卡号
     * 非空，最大长度20，唯一
     */
    @Column(name = "card_id", nullable = false, length = 20, unique = true)
    private String cardId;

    /**
     * 用户角色
     * 非空，最大长度10
     */
    @Column(name = "role", nullable = false, length = 10)
    private String role;

    /**
     * 用户密码
     * 非空，最大长度100
     */
    @Column(name = "password", nullable = false, length = 100)
    private String password;

    // Getter and Setter ↓↓↓↓

    /**
     * 获取用户ID
     * @return 用户ID
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置用户ID
     * @param id 用户ID
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取用户姓名
     * @return 用户姓名
     */
    public String getName() {
        return name;
    }

    /**
     * 设置用户姓名
     * @param name 用户姓名
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取用户卡号
     * @return 用户卡号
     */
    public String getCardId() {
        return cardId;
    }

    /**
     * 设置用户卡号
     * @param cardId 用户卡号
     */
    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    /**
     * 获取用户角色
     * @return 用户角色
     */
    public String getRole() {
        return role;
    }

    /**
     * 设置用户角色
     * @param role 用户角色
     */
    public void setRole(String role) {
        this.role = role;
    }

    /**
     * 获取用户密码
     * @return 用户密码
     */
    public String getPassword() {
        return password;
    }

    /**
     * 设置用户密码
     * @param password 用户密码
     */
    public void setPassword(String password) {
        this.password = password;
    }

    // UserDetails methods ↓↓↓↓

    /**
     * 获取用户权限
     * 将用户角色转换为Spring Security的权限格式
     * @return 用户权限集合
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(() -> "ROLE_" + role);
    }

    /**
     * 获取用户名
     * 使用卡号作为用户名
     * @return 用户卡号
     */
    @Override
    public String getUsername() {
        return cardId;
    }

    /**
     * 检查账户是否未过期
     * @return 始终返回true
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * 检查账户是否未锁定
     * @return 始终返回true
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * 检查凭证是否未过期
     * @return 始终返回true
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * 检查账户是否启用
     * @return 始终返回true
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}
