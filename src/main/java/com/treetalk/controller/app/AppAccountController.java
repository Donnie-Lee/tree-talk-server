package com.treetalk.controller.app;

import com.treetalk.model.dto.AccountProfileInfo;
import com.treetalk.model.dto.ApiResponse;
import com.treetalk.model.dto.AuthDto;
import com.treetalk.model.dto.RegisterInfo;
import com.treetalk.model.entity.Account;
import com.treetalk.service.AccountService;
import com.treetalk.service.CheckCodeService;
import com.treetalk.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 类描述：
 *
 * @author lizheng
 * @created 2025/9/1 09:55
 */
@RestController
@RequestMapping("/app/account")
@Tag(name = "移动端-账户管理", description = "移动端-账户相关信息")
public record AppAccountController(JwtUtil jwtUtil,
                                   AccountService accountService,
                                   CheckCodeService checkCodeService) {

    @PostMapping("login")
    @Operation(summary = "密码/验证码登录", description = "用户登录")
    public ApiResponse<String> login(@RequestBody AuthDto.LoginDto login) {
        return ApiResponse.success(jwtUtil.generateToken(accountService.login(login)));
    }

    @Operation(summary = "注册账户", description = "注册账户")
    @PostMapping("/register")
    public ApiResponse<Void> registerAccount(@RequestBody RegisterInfo registerInfo) {
        accountService.registerAccount(registerInfo);
        return ApiResponse.message("账户注册成功");
    }

    @PostMapping("logout")
    @Operation(summary = "登出", description = "退出系统")
    public ApiResponse<List<Account>> getAllAccounts() {
        List<Account> accounts = accountService.getAllAccounts();
        return ApiResponse.success(accounts);
    }

    @Operation(summary = "获取当前账户", description = "获取当前账户信息")
    @GetMapping("/current")
    public ApiResponse<AccountProfileInfo> currentAccount() {
        return ApiResponse.success("获取当前账户成功", accountService.currentAccount());
    }

    @Operation(summary = "修改账户信息", description = "修改账户信息")
    @PostMapping("/modify")
    public ApiResponse<Void> modify(@RequestBody AccountProfileInfo accountProfileInfo) {
        accountService.modify(accountProfileInfo);
        return ApiResponse.success();
    }

}