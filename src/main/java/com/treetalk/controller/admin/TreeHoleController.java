package com.treetalk.controller.admin;

import com.treetalk.model.dto.ApiResponse;
import com.treetalk.model.entity.TreeHole;
import com.treetalk.service.TreeHoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/treeholes")
@Tag(name = "树洞管理", description = "树洞帖子相关操作接口")
public class TreeHoleController {
    
    @Autowired
    private TreeHoleService treeHoleService;
    
    @GetMapping
    @Operation(summary = "获取所有树洞帖子", description = "获取系统中的所有树洞帖子")
    public ApiResponse<List<TreeHole>> getAllTreeHoles() {
        List<TreeHole> treeHoles = treeHoleService.getAllTreeHoles();
        return ApiResponse.success(treeHoles);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "根据ID获取树洞帖子", description = "根据帖子ID获取特定树洞帖子的信息")
    public ApiResponse<TreeHole> getTreeHoleById(
            @Parameter(description = "树洞帖子ID") @PathVariable Long id) {
        return treeHoleService.getTreeHoleById(id)
                .map(treeHole -> ApiResponse.success(treeHole))
                .orElseGet(() -> ApiResponse.error("TreeHole not found"));
    }
    
    @GetMapping("/account/{accountId}")
    @Operation(summary = "根据账户ID获取树洞帖子", description = "根据账户ID获取该用户发布的所有树洞帖子")
    public ApiResponse<List<TreeHole>> getTreeHolesByAccountId(
            @Parameter(description = "账户ID") @PathVariable Long accountId) {
        List<TreeHole> treeHoles = treeHoleService.getTreeHolesByAccountId(accountId);
        return ApiResponse.success(treeHoles);
    }
    
    @PostMapping
    @Operation(summary = "创建树洞帖子", description = "创建一个新的树洞帖子")
    public ApiResponse<TreeHole> createTreeHole(
            @Parameter(description = "树洞帖子信息") @RequestBody TreeHole treeHole) {
        TreeHole savedTreeHole = treeHoleService.saveTreeHole(treeHole);
        return ApiResponse.success("TreeHole created successfully", savedTreeHole);
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "更新树洞帖子", description = "根据帖子ID更新树洞帖子内容")
    public ApiResponse<TreeHole> updateTreeHole(
            @Parameter(description = "树洞帖子ID") @PathVariable Long id,
            @Parameter(description = "更新的树洞帖子信息") @RequestBody TreeHole treeHole) {
        treeHole.setId(id);
        TreeHole updatedTreeHole = treeHoleService.saveTreeHole(treeHole);
        return ApiResponse.success("TreeHole updated successfully", updatedTreeHole);
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "删除树洞帖子", description = "根据帖子ID删除树洞帖子")
    public ApiResponse<Void> deleteTreeHole(
            @Parameter(description = "树洞帖子ID") @PathVariable Long id) {
        treeHoleService.deleteTreeHole(id);
        return ApiResponse.success("TreeHole deleted successfully", null);
    }
}