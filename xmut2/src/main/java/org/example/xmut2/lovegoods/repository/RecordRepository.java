package org.example.xmut2.lovegoods.repository;

import org.example.xmut2.lovegoods.model.TakeRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecordRepository extends JpaRepository<TakeRecord, Long> {
    List<TakeRecord> findByUserIdAndType(Long userId, String type);
    // 自定义 SQL 查询每月记录
    @Query(value = "SELECT * FROM record WHERE  type = 'TAKE'", nativeQuery = true)
    List<TakeRecord> findByYearMonth(String yearMonth);

    List<TakeRecord> findByType(String type);
}
