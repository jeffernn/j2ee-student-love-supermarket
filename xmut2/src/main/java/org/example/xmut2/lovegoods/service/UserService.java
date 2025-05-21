package org.example.xmut2.lovegoods.service;

import jakarta.annotation.PostConstruct;
import org.example.xmut2.lovegoods.model.User;
import org.example.xmut2.lovegoods.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @PostConstruct
    public void initAdminUser() {
        if (userRepository.findByCardId("admin001") == null) {
            User admin = new User();
            admin.setName("管理员");
            admin.setCardId("admin001");
            admin.setRole("ADMIN");
            admin.setPassword(passwordEncoder.encode("123456"));
            userRepository.save(admin);
        }
    }

    // 根据卡号查找用户
    public User findByCardId(String cardId) {
        return userRepository.findByCardId(cardId);
    }

    // 比对密码是否正确
    public boolean checkPassword(User user, String rawPassword) {
        return passwordEncoder.matches(rawPassword, user.getPassword());
    }

    // 获取所有用户（可选）
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
