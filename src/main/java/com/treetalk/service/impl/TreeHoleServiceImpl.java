package com.treetalk.service.impl;

import com.treetalk.model.entity.TreeHole;
import com.treetalk.repository.TreeHoleRepository;
import com.treetalk.service.TreeHoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TreeHoleServiceImpl implements TreeHoleService {
    
    @Autowired
    private TreeHoleRepository treeHoleRepository;
    
    @Override
    public List<TreeHole> getAllTreeHoles() {
        return treeHoleRepository.findAll();
    }
    
    @Override
    public List<TreeHole> getTreeHolesByAccountId(Long accountId) {
        return treeHoleRepository.findByAccountId(accountId);
    }
    
    @Override
    public Optional<TreeHole> getTreeHoleById(Long id) {
        return treeHoleRepository.findById(id);
    }
    
    @Override
    public TreeHole saveTreeHole(TreeHole treeHole) {
        return treeHoleRepository.save(treeHole);
    }
    
    @Override
    public void deleteTreeHole(Long id) {
        treeHoleRepository.deleteById(id);
    }
}