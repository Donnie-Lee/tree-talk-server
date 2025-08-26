package com.treetalk.repository;

import com.treetalk.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByPhone(String phone);

    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);

    @Query("SELECT u FROM User u WHERE (u.email = :login OR u.phone = :login) and u.isDeleted = false")
    Optional<User> findByEmailOrPhone(@Param("login") String login);

    @Query("SELECT u FROM User u WHERE u.id = :userId AND u.isDeleted = false")
    Optional<User> findActiveUserById(@Param("userId") Long userId);
}