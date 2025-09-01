package com.treetalk.repository;

import com.treetalk.model.entity.AccountProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 类描述：
 *
 * @author lizheng
 * @created 2025/9/1 10:02
 */
@Repository
public interface AccountProfileRepository extends JpaRepository<AccountProfile, Long> {
    Optional<AccountProfile> findByAccountId(Long accountId);
}
