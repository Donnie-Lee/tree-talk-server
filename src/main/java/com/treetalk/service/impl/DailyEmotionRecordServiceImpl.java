package com.treetalk.service.impl;

import com.treetalk.model.entity.DailyEmotionRecord;
import com.treetalk.repository.DailyEmotionRecordRepository;
import com.treetalk.service.DailyEmotionRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DailyEmotionRecordServiceImpl implements DailyEmotionRecordService {
    
    @Autowired
    private DailyEmotionRecordRepository emotionRecordRepository;
    
    @Override
    public List<DailyEmotionRecord> getAllEmotionRecords() {
        return emotionRecordRepository.findAll();
    }
    
    @Override
    public List<DailyEmotionRecord> getEmotionRecordsByAccountId(Long accountId) {
        return emotionRecordRepository.findByAccountId(accountId);
    }
    
    @Override
    public Optional<DailyEmotionRecord> getEmotionRecordById(Long id) {
        return emotionRecordRepository.findById(id);
    }
    
    @Override
    public DailyEmotionRecord saveEmotionRecord(DailyEmotionRecord emotionRecord) {
        return emotionRecordRepository.save(emotionRecord);
    }
    
    @Override
    public void deleteEmotionRecord(Long id) {
        emotionRecordRepository.deleteById(id);
    }
}