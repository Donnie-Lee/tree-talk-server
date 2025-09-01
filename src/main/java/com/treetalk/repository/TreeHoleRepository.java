package com.treetalk.repository;

import com.treetalk.model.entity.TreeHole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TreeHoleRepository extends JpaRepository<TreeHole, Long> {
    List<TreeHole> findByAccountId(Long accountId);
}