package com.treetalk.repository;

import com.treetalk.entity.EmotionRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface EmotionRecordRepository extends JpaRepository<EmotionRecord, Long> {

    List<EmotionRecord> findByUserIdAndRecordDateOrderByRecordTime(Long userId, LocalDate recordDate);

    Optional<EmotionRecord> findByUserIdAndRecordDate(Long userId, LocalDate recordDate);

    List<EmotionRecord> findByUserIdAndRecordDateBetweenOrderByRecordDateDesc(
            Long userId, LocalDate startDate, LocalDate endDate);

    @Query("SELECT er FROM EmotionRecord er WHERE er.user.id = :userId AND er.recordDate = CURRENT_DATE AND er.isDeleted = false")
    Optional<EmotionRecord> findTodayEmotionRecord(@Param("userId") Long userId);

    @Query("SELECT er FROM EmotionRecord er WHERE er.user.id = :userId AND er.isDeleted = false ORDER BY er.recordDate DESC")
    Page<EmotionRecord> findActiveEmotionRecordsByUserId(@Param("userId") Long userId, Pageable pageable);

    @Query("SELECT COUNT(er) FROM EmotionRecord er WHERE er.user.id = :userId AND er.recordDate = :date AND er.isDeleted = false")
    long countByUserIdAndRecordDate(@Param("userId") Long userId, @Param("date") LocalDate date);
}