package org.example.xmut2.lovegoods.service;

import org.example.xmut2.lovegoods.model.User;
import org.example.xmut2.lovegoods.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * 自定义用户详情服务类
 * 实现Spring Security的UserDetailsService接口
 * 用于加载用户认证信息
 */
@Service
public class MyUserDetailService implements UserDetailsService {

    /**
     * 用户数据访问对象
     * 用于查询用户信息
     */
    private final UserRepository userRepository;

    /**
     * 构造函数
     * 通过依赖注入获取UserRepository实例
     * @param userRepository 用户数据访问对象
     */
    public MyUserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * 根据用户名（卡号）加载用户详情
     * 实现UserDetailsService接口的方法
     * @param cardId 用户卡号
     * @return UserDetails对象，包含用户认证信息
     * @throws UsernameNotFoundException 当用户不存在时抛出此异常
     */
    @Override
    public UserDetails loadUserByUsername(String cardId) throws UsernameNotFoundException {
        // 根据卡号查询用户
        User user = userRepository.findByCardId(cardId);
        if (user == null) {
            throw new UsernameNotFoundException("用户不存在");
        }
        // 构建UserDetails对象
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getCardId())
                .password(user.getPassword())
                .roles(user.getRole()) // 注意：Spring 会自动加上 ROLE_ 前缀
                .build();
    }
}

