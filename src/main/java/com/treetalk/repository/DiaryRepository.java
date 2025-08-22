package com.treetalk.repository;

import com.treetalk.entity.Diary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DiaryRepository extends JpaRepository<Diary, Long> {

    @Query("SELECT d FROM Diary d WHERE d.user.id = :userId AND d.isDeleted = false ORDER BY d.recordDate DESC")
    List<Diary> findByUserIdOrderByRecordDateDesc(@Param("userId") Long userId);

    @Query("SELECT d FROM Diary d WHERE d.user.id = :userId AND d.recordDate = :date AND d.isDeleted = false")
    Optional<Diary> findByUserIdAndRecordDate(@Param("userId") Long userId, @Param("date") LocalDate date);

    @Query("SELECT d FROM Diary d WHERE d.user.id = :userId AND d.recordDate BETWEEN :startDate AND :endDate AND d.isDeleted = false ORDER BY d.recordDate DESC")
    List<Diary> findByUserIdAndRecordDateBetweenOrderByRecordDateDesc(
            @Param("userId") Long userId, 
            @Param("startDate") LocalDate startDate, 
            @Param("endDate") LocalDate endDate);

    @Query("SELECT COUNT(d) FROM Diary d WHERE d.user.id = :userId AND d.isDeleted = false")
    long countByUserId(@Param("userId") Long userId);

    @Query("SELECT d FROM Diary d WHERE d.id = :id AND d.isDeleted = false")
    Optional<Diary> findActiveDiaryById(@Param("id") Long id);
}