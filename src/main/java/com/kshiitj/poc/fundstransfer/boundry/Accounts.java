package com.kshiitj.poc.fundstransfer.boundry;

import com.kshiitj.poc.fundstransfer.domain.Account;
import com.kshiitj.poc.fundstransfer.exceptions.NoAccountAvailableException;
import com.kshiitj.poc.fundstransfer.service.AccountService;

import javax.security.auth.login.AccountNotFoundException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class Accounts {
    private AccountService accountService;
    public Accounts(AccountService accountService){
        this.accountService=accountService;
    }
    public UUID createAccount(BigDecimal initialBalance) {
        return this.accountService.createAccount(initialBalance).getId();
    }

    public List<Account> getAccounts() {
        return this.accountService.getAccounts().orElseThrow(()->new NoAccountAvailableException());
    }
}
