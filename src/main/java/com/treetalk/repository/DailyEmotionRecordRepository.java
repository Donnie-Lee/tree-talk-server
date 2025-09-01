package com.treetalk.repository;

import com.treetalk.model.entity.DailyEmotionRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DailyEmotionRecordRepository extends JpaRepository<DailyEmotionRecord, Long> {
    List<DailyEmotionRecord> findByAccountId(Long accountId);
}