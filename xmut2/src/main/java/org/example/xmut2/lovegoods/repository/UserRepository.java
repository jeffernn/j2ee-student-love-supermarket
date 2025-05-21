package org.example.xmut2.lovegoods.repository;

import org.example.xmut2.lovegoods.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByCardId(String cardId);
}