package com.treetalk.service;

import com.treetalk.model.dto.AccountProfileInfo;
import com.treetalk.model.dto.AuthDto;
import com.treetalk.model.dto.RegisterInfo;
import com.treetalk.model.entity.Account;

import java.util.List;
import java.util.Optional;

public interface AccountService {
    List<Account> getAllAccounts();

    Optional<Account> getAccountById(Long id);

    Account saveAccount(Account account);

    void deleteAccount(Long id);

    Account login(AuthDto.LoginDto login);

    AccountProfileInfo currentAccount();

    void registerAccount(RegisterInfo registerInfo);

    void modify(AccountProfileInfo accountProfileInfo);

}