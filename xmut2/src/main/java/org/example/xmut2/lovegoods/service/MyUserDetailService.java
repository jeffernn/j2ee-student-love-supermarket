package org.example.xmut2.lovegoods.service;

import org.example.xmut2.lovegoods.model.User;
import org.example.xmut2.lovegoods.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailService implements UserDetailsService {

    // 假设你已有一个 UserRepository 或 UserService 可以查询用户
    private final UserRepository  userRepository;

    public MyUserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String cardId) throws UsernameNotFoundException {
        User user = userRepository.findByCardId(cardId);
        if (user == null) {
            throw new UsernameNotFoundException("用户不存在");
        }
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getCardId())
                .password(user.getPassword())
                .roles(user.getRole()) // 注意：Spring 会自动加上 ROLE_ 前缀
                .build();
    }
}

