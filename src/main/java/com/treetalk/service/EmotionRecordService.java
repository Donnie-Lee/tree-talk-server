package com.treetalk.service;

import com.treetalk.dto.EmotionRecordDto;
import com.treetalk.entity.EmotionRecord;
import com.treetalk.entity.User;
import com.treetalk.repository.EmotionRecordRepository;
import com.treetalk.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class EmotionRecordService {

    private final EmotionRecordRepository emotionRecordRepository;
    private final UserRepository userRepository;

    @Autowired
    public EmotionRecordService(EmotionRecordRepository emotionRecordRepository,
                               UserRepository userRepository) {
        this.emotionRecordRepository = emotionRecordRepository;
        this.userRepository = userRepository;
    }

    public EmotionRecordDto.Response createEmotionRecord(Long userId, EmotionRecordDto.CreateRequest request) {
        User user = userRepository.findActiveUserById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        // 检查今天是否已经记录过情绪
        if (emotionRecordRepository.findTodayEmotionRecord(userId).isPresent()) {
            throw new RuntimeException("今天已经记录过情绪了");
        }

        EmotionRecord record = new EmotionRecord();
        record.setUser(user);
        record.setEmotionType(request.getEmotionType());
        record.setEmotionValue(request.getEmotionValue());
        record.setRecordDate(LocalDate.now());
        record.setRecordTime(LocalTime.now());

        record = emotionRecordRepository.save(record);
        return convertToResponse(record);
    }

    public EmotionRecordDto.Response getTodayEmotionRecord(Long userId) {
        return emotionRecordRepository.findTodayEmotionRecord(userId)
                .map(this::convertToResponse)
                .orElse(null);
    }

    public List<EmotionRecordDto.Response> getEmotionRecordsByDateRange(Long userId, LocalDate startDate, LocalDate endDate) {
        List<EmotionRecord> records = emotionRecordRepository
                .findByUserIdAndRecordDateBetweenOrderByRecordDateDesc(userId, startDate, endDate);
        
        return records.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    private EmotionRecordDto.Response convertToResponse(EmotionRecord record) {
        EmotionRecordDto.Response response = new EmotionRecordDto.Response();
        response.setId(record.getId());
        response.setEmotionType(record.getEmotionType());
        response.setEmotionValue(record.getEmotionValue());
        response.setRecordDate(record.getRecordDate());
        response.setRecordTime(record.getRecordTime());
        return response;
    }
}