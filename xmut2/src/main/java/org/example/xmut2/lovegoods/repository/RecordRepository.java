package org.example.xmut2.lovegoods.repository;

import org.example.xmut2.lovegoods.model.TakeRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecordRepository extends JpaRepository<TakeRecord, Long> {
    List<TakeRecord> findByUserIdAndType(Long userId, String type);
    
    // 简单查询所有TAKE类型记录，然后在Java中进行处理
    List<TakeRecord> findByType(String type);
}
