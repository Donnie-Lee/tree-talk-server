package com.treetalk.service;

import com.treetalk.model.entity.DailyEmotionRecord;

import java.util.List;
import java.util.Optional;

public interface DailyEmotionRecordService {
    List<DailyEmotionRecord> getAllEmotionRecords();
    List<DailyEmotionRecord> getEmotionRecordsByAccountId(Long accountId);
    Optional<DailyEmotionRecord> getEmotionRecordById(Long id);
    DailyEmotionRecord saveEmotionRecord(DailyEmotionRecord emotionRecord);
    void deleteEmotionRecord(Long id);
}