package com.treetalk.repository;

import com.treetalk.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {

    Optional<UserProfile> findByUserId(Long userId);

    @Query("SELECT up FROM UserProfile up WHERE up.user.id = :userId AND up.isDeleted = false")
    Optional<UserProfile> findActiveProfileByUserId(@Param("userId") Long userId);

    boolean existsByUserId(Long userId);
}