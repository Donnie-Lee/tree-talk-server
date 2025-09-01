package com.treetalk.service.impl;

import com.treetalk.model.entity.Diary;
import com.treetalk.repository.DiaryRepository;
import com.treetalk.service.DiaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DiaryServiceImpl implements DiaryService {
    
    @Autowired
    private DiaryRepository diaryRepository;
    
    @Override
    public List<Diary> getAllDiaries() {
        return diaryRepository.findAll();
    }
    
    @Override
    public List<Diary> getDiariesByAccountId(Long accountId) {
        return diaryRepository.findByAccountId(accountId);
    }
    
    @Override
    public Optional<Diary> getDiaryById(Long id) {
        return diaryRepository.findById(id);
    }
    
    @Override
    public Diary saveDiary(Diary diary) {
        return diaryRepository.save(diary);
    }
    
    @Override
    public void deleteDiary(Long id) {
        diaryRepository.deleteById(id);
    }
}