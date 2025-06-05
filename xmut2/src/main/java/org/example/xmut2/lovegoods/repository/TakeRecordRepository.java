package org.example.xmut2.lovegoods.repository;

import org.example.xmut2.lovegoods.model.TakeRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TakeRecordRepository extends JpaRepository<TakeRecord, Long> {
    List<TakeRecord> findByUserIdAndType(Long userId, String type);
}
