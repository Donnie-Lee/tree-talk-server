package com.treetalk.repository;

import com.treetalk.model.entity.CheckCodeConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 类描述：
 *
 * @author lizheng
 * @created 2025/8/29 16:46
 */
@Repository
public interface CheckCodeConfigRepository extends JpaRepository<CheckCodeConfig, Long> {
    Optional<CheckCodeConfig> findBySceneCodeAndIsEnabledIsTrue(String smsCode);
}
