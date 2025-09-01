package com.treetalk.service;

import com.treetalk.model.entity.Diary;

import java.util.List;
import java.util.Optional;

public interface DiaryService {
    List<Diary> getAllDiaries();
    List<Diary> getDiariesByAccountId(Long accountId);
    Optional<Diary> getDiaryById(Long id);
    Diary saveDiary(Diary diary);
    void deleteDiary(Long id);
}