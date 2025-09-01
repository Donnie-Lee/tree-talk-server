package com.treetalk.service.impl;

import com.treetalk.model.dto.AccountProfileInfo;
import com.treetalk.model.dto.AuthDto;
import com.treetalk.model.dto.RegisterInfo;
import com.treetalk.model.entity.Account;
import com.treetalk.model.entity.AccountProfile;
import com.treetalk.model.enums.LoginType;
import com.treetalk.repository.AccountProfileRepository;
import com.treetalk.repository.AccountRepository;
import com.treetalk.service.AccountService;
import com.treetalk.service.CheckCodeService;
import com.treetalk.util.SecurityUserUtils;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AccountServiceImpl implements AccountService {

    @Resource
    private AccountRepository accountRepository;
    @Resource
    private AccountProfileRepository accountProfileRepository;
    @Resource
    private PasswordEncoder passwordEncoder;
    @Autowired
    private CheckCodeService checkCodeService;

    @Override
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    @Override
    public Optional<Account> getAccountById(Long id) {
        return accountRepository.findById(id);
    }

    @Override
    public Account saveAccount(Account account) {
        return accountRepository.save(account);
    }

    @Override
    public void deleteAccount(Long id) {
        accountRepository.deleteById(id);
    }

    @Override
    public Account login(AuthDto.LoginDto login) {
        Account account = accountRepository.findByUsername(login.username())
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        // 账号密码登录
        if (LoginType.PASSWORD.equals(login.loginType())) {
            // 账号密码登录
            if (!passwordEncoder.matches(login.password(), account.getPassword())) {
                throw new RuntimeException("密码错误");
            }
        } else {
            // 验证码登录
            checkCodeService.verifyCode("login",login.username(), login.password());
        }
        account.setLastLoginTime(LocalDateTime.now());
        accountRepository.save(account);
        return account;
    }

    @Override
    public AccountProfileInfo currentAccount() {
        AccountProfile accountProfile = accountProfileRepository.findByAccountId(SecurityUserUtils.getCurrentUserId())
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        // 校验状态
        if (accountProfile.getAccount().getStatus() != 1) {
            throw new RuntimeException("用户被禁用");
        }

        AccountProfileInfo accountProfileInfo = new AccountProfileInfo();
        BeanUtils.copyProperties(accountProfile.getAccount(), accountProfileInfo);
        BeanUtils.copyProperties(accountProfile, accountProfileInfo);
        return accountProfileInfo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void registerAccount(RegisterInfo registerInfo) {
        // 校验手机号是否存在
        accountRepository.findByUsername(registerInfo.username())
                .ifPresent(account -> {
                    throw new RuntimeException("手机号已存在");
                });
        // 校验密码是否输入一致
        if (!registerInfo.password().equals(registerInfo.repeatPassword())) {
            throw new RuntimeException("密码不一致");
        }
        // 校验验证码
        checkCodeService.verifyCode("register",registerInfo.username(), registerInfo.checkCode());

        Account account = new Account();
        account.setUsername(registerInfo.username());
        account.setPassword(passwordEncoder.encode(registerInfo.password()));
        account.setStatus(1);
        account = accountRepository.save(account);

        AccountProfile accountProfile = new AccountProfile();
        accountProfile.setAccount(account);
        accountProfileRepository.save(accountProfile);
    }

    @Override
    public void modify(AccountProfileInfo accountProfileInfo) {
        accountProfileRepository.findByAccountId(SecurityUserUtils.getCurrentUserId())
                .ifPresent(accountProfile -> {
                    BeanUtils.copyProperties(accountProfileInfo, accountProfile);
                    accountProfileRepository.save(accountProfile);

                    accountProfile.getAccount().setBirthday(accountProfileInfo.getBirthday());
                    accountProfile.getAccount().setSignature(accountProfileInfo.getSignature());
                    accountProfile.getAccount().setNickname(accountProfileInfo.getNickname());
                    accountProfile.getAccount().setIdCard(accountProfileInfo.getIdCard());
                    accountProfile.getAccount().setAddress(accountProfileInfo.getAddress());
                    accountProfile.getAccount().setGender(accountProfileInfo.getGender());
                    accountRepository.save(accountProfile.getAccount());
                });
    }

}