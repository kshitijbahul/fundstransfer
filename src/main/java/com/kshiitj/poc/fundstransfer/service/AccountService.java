package com.kshiitj.poc.fundstransfer.service;

import com.kshiitj.poc.fundstransfer.domain.Account;
import com.kshiitj.poc.fundstransfer.exceptions.AccountNotFoundException;
import com.kshiitj.poc.fundstransfer.exceptions.InsufficientBalanceException;
import com.kshiitj.poc.fundstransfer.store.AccountStore;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
@Singleton
public class AccountService {
    private AccountStore accountStore;

    @Inject
    public AccountService(AccountStore accountStore){
        this.accountStore=accountStore;
    }
    public Account getAccount(UUID accountId) throws AccountNotFoundException{
        return this.accountStore.getAccount(accountId);
    }
    public Account createAccount(BigDecimal initialBalance) {
        Account account=new Account(initialBalance);
        this.accountStore.saveAccount(account);
        return account;
    }
    public Account withdraw(UUID accountId,BigDecimal amount) throws AccountNotFoundException {
        Account account=this.accountStore.getAccount(accountId);
        account.withdraw(amount);
        this.accountStore.saveAccount(account);
        return account;
    }

    public Account deposit(UUID accountId,BigDecimal amount) throws AccountNotFoundException {
        Account account=this.accountStore.getAccount(accountId);
        account.deposit(amount);
        this.accountStore.saveAccount(account);
        return account;
    }

    public Optional<List<Account>> getAccounts() {
        return Optional.of(this.accountStore.getAllAccounts());
    }
}
