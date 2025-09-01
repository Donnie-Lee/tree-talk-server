package com.treetalk.service;

import com.treetalk.model.entity.TreeHole;

import java.util.List;
import java.util.Optional;

public interface TreeHoleService {
    List<TreeHole> getAllTreeHoles();
    List<TreeHole> getTreeHolesByAccountId(Long accountId);
    Optional<TreeHole> getTreeHoleById(Long id);
    TreeHole saveTreeHole(TreeHole treeHole);
    void deleteTreeHole(Long id);
}