package com.treetalk.service;

import com.treetalk.dto.DiaryDto;
import com.treetalk.dto.EmotionRecordDto;
import com.treetalk.entity.Diary;
import com.treetalk.entity.EmotionRecord;
import com.treetalk.entity.User;
import com.treetalk.repository.DiaryRepository;
import com.treetalk.repository.EmotionRecordRepository;
import com.treetalk.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class DiaryService {

    private final DiaryRepository diaryRepository;
    private final UserRepository userRepository;
    private final EmotionRecordRepository emotionRecordRepository;

    @Autowired
    public DiaryService(DiaryRepository diaryRepository,
                       UserRepository userRepository,
                       EmotionRecordRepository emotionRecordRepository) {
        this.diaryRepository = diaryRepository;
        this.userRepository = userRepository;
        this.emotionRecordRepository = emotionRecordRepository;
    }

    public DiaryDto.Response createDiary(Long userId, DiaryDto.CreateRequest request) {
        User user = userRepository.findActiveUserById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        EmotionRecord emotionRecord = emotionRecordRepository.findByUserIdAndRecordDate(userId, LocalDate.now())
                .orElseThrow(() -> new RuntimeException("情绪记录不存在"));

        // 检查今天是否已经写过日记
        if (diaryRepository.findByUserIdAndRecordDate(userId, LocalDate.now()).isPresent()) {
            throw new RuntimeException("今天已经写过日记了");
        }

        Diary diary = new Diary();
        diary.setUser(user);
        diary.setEmotionRecord(emotionRecord);
        diary.setContent(request.getContent());
        diary.setIsEncrypted(request.isEncrypted());
        diary.setEncryptedContent(request.getEncryptedContent());
        diary.setRecordDate(LocalDate.now());

        diary = diaryRepository.save(diary);
        return convertToResponse(diary);
    }

    public DiaryDto.Response updateDiary(Long userId, Long diaryId, DiaryDto.UpdateRequest request) {
        Diary diary = diaryRepository.findActiveDiaryById(diaryId)
                .orElseThrow(() -> new RuntimeException("日记不存在"));

        if (!diary.getUser().getId().equals(userId)) {
            throw new RuntimeException("无权修改他人的日记");
        }

        diary.setContent(request.getContent());
        diary.setIsEncrypted(request.isEncrypted());
        diary.setEncryptedContent(request.getEncryptedContent());

        diary = diaryRepository.save(diary);
        return convertToResponse(diary);
    }

    public DiaryDto.Response getDiaryById(Long userId, Long diaryId) {
        Diary diary = diaryRepository.findActiveDiaryById(diaryId)
                .orElseThrow(() -> new RuntimeException("日记不存在"));

        if (!diary.getUser().getId().equals(userId)) {
            throw new RuntimeException("无权查看他人的日记");
        }

        return convertToResponse(diary);
    }

    public List<DiaryDto.ListResponse> getDiariesByDateRange(Long userId, LocalDate startDate, LocalDate endDate) {
        List<Diary> diaries = diaryRepository
                .findByUserIdAndRecordDateBetweenOrderByRecordDateDesc(userId, startDate, endDate);
        
        return diaries.stream()
                .map(this::convertToListResponse)
                .collect(Collectors.toList());
    }

    public List<DiaryDto.ListResponse> getAllDiaries(Long userId) {
        List<Diary> diaries = diaryRepository.findByUserIdOrderByRecordDateDesc(userId);
        return diaries.stream()
                .map(this::convertToListResponse)
                .collect(Collectors.toList());
    }

    public void deleteDiary(Long userId, Long diaryId) {
        Diary diary = diaryRepository.findActiveDiaryById(diaryId)
                .orElseThrow(() -> new RuntimeException("日记不存在"));

        if (!diary.getUser().getId().equals(userId)) {
            throw new RuntimeException("无权删除他人的日记");
        }

        diary.setIsDeleted(true);
        diaryRepository.save(diary);
    }

    private DiaryDto.Response convertToResponse(Diary diary) {
        DiaryDto.Response response = new DiaryDto.Response();
        response.setId(diary.getId());
        response.setEmotionRecordId(diary.getEmotionRecord() != null ? diary.getEmotionRecord().getId() : null);
        response.setContent(diary.getContent());
        response.setEncrypted(diary.getIsEncrypted());
        response.setEncryptedContent(diary.getEncryptedContent());
        response.setRecordDate(diary.getRecordDate());
        
        if (diary.getEmotionRecord() != null) {
            EmotionRecordDto.Response emotionResponse = new EmotionRecordDto.Response();
            emotionResponse.setId(diary.getEmotionRecord().getId());
            emotionResponse.setEmotionType(diary.getEmotionRecord().getEmotionType());
            emotionResponse.setEmotionValue(diary.getEmotionRecord().getEmotionValue());
            emotionResponse.setRecordDate(diary.getEmotionRecord().getRecordDate());
            emotionResponse.setRecordTime(diary.getEmotionRecord().getRecordTime());
            response.setEmotionRecord(emotionResponse);
        }
        
        return response;
    }

    private DiaryDto.ListResponse convertToListResponse(Diary diary) {
        DiaryDto.ListResponse response = new DiaryDto.ListResponse();
        response.setId(diary.getId());
        response.setRecordDate(diary.getRecordDate());
        
        if (diary.getEmotionRecord() != null) {
            EmotionRecordDto.Response emotionResponse = new EmotionRecordDto.Response();
            emotionResponse.setId(diary.getEmotionRecord().getId());
            emotionResponse.setEmotionType(diary.getEmotionRecord().getEmotionType());
            emotionResponse.setEmotionValue(diary.getEmotionRecord().getEmotionValue());
            emotionResponse.setRecordDate(diary.getEmotionRecord().getRecordDate());
            emotionResponse.setRecordTime(diary.getEmotionRecord().getRecordTime());
            response.setEmotionRecord(emotionResponse);
        }
        
        // 创建预览内容（前50个字符）
        String content = diary.getContent();
        if (content != null && content.length() > 50) {
            response.setPreviewContent(content.substring(0, 50) + "...");
        } else {
            response.setPreviewContent(content);
        }
        
        return response;
    }
}