package com.treetalk.controller.admin;

import com.treetalk.model.dto.ApiResponse;
import com.treetalk.model.entity.Account;
import com.treetalk.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@Tag(name = "账户管理", description = "用户账户相关操作接口")
public class AccountController {
    
    @Autowired
    private AccountService accountService;
    
    @GetMapping
    @Operation(summary = "获取所有账户", description = "获取系统中的所有账户信息")
    public ApiResponse<List<Account>> getAllAccounts() {
        List<Account> accounts = accountService.getAllAccounts();
        return ApiResponse.success(accounts);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "根据ID获取账户", description = "根据账户ID获取特定账户的信息")
    public ApiResponse<Account> getAccountById(
            @Parameter(description = "账户ID") @PathVariable Long id) {
        return accountService.getAccountById(id)
                .map(account -> ApiResponse.success(account))
                .orElseGet(() -> ApiResponse.error("Account not found"));
    }
    
    @PostMapping
    @Operation(summary = "创建账户", description = "创建一个新的账户")
    public ApiResponse<Account> createAccount(
            @Parameter(description = "账户信息") @RequestBody Account account) {
        Account savedAccount = accountService.saveAccount(account);
        return ApiResponse.success("Account created successfully", savedAccount);
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "更新账户", description = "根据账户ID更新账户信息")
    public ApiResponse<Account> updateAccount(
            @Parameter(description = "账户ID") @PathVariable Long id,
            @Parameter(description = "更新的账户信息") @RequestBody Account account) {
        // In a real application, you should check if the account exists first
        account.setId(id);
        Account updatedAccount = accountService.saveAccount(account);
        return ApiResponse.success("Account updated successfully", updatedAccount);
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "删除账户", description = "根据账户ID删除账户")
    public ApiResponse<Void> deleteAccount(
            @Parameter(description = "账户ID") @PathVariable Long id) {
        accountService.deleteAccount(id);
        return ApiResponse.success("Account deleted successfully", null);
    }
}