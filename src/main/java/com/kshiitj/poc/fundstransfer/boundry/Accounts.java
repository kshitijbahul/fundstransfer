package com.kshiitj.poc.fundstransfer.boundry;

import com.kshiitj.poc.fundstransfer.domain.Account;
import com.kshiitj.poc.fundstransfer.domain.Transaction;
import com.kshiitj.poc.fundstransfer.exceptions.AccountNotFoundException;
import com.kshiitj.poc.fundstransfer.exceptions.NoAccountAvailableException;
import com.kshiitj.poc.fundstransfer.exceptions.NoTransactionsAvailableException;
import com.kshiitj.poc.fundstransfer.service.AccountService;


import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class Accounts {
    private AccountService accountService;
    public Accounts(AccountService accountService){
        this.accountService=accountService;
    }
    public UUID createAccount(BigDecimal initialBalance) throws AccountNotFoundException {
        return this.accountService.createAccount(initialBalance).getId();
    }

    public List<Account> getAccounts() {
        return this.accountService.getAccounts().orElseThrow(()->new NoAccountAvailableException());
    }
    public List<Transaction> getAccountTransactions(UUID accountId){
        return this.accountService.getAccountTransactions(accountId).orElseThrow(()-> new NoTransactionsAvailableException());
    }
    public Account getAccount(UUID accountId) throws AccountNotFoundException {
        return this.accountService.getAccount(accountId);
    }
}
