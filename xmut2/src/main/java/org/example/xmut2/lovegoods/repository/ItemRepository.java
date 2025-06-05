package org.example.xmut2.lovegoods.repository;

import org.example.xmut2.lovegoods.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findByCategory(String category);
    Item findByQrCode(String qrCode);

    List<Item> findByStatus(String status);
    List<Item> findByCategoryAndIsStored(String category, Boolean isStored);

}