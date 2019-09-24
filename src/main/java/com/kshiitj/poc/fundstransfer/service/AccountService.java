package com.kshiitj.poc.fundstransfer.service;

import com.kshiitj.poc.fundstransfer.domain.Account;
import com.kshiitj.poc.fundstransfer.domain.Transaction;
import com.kshiitj.poc.fundstransfer.domain.TransactionType;
import com.kshiitj.poc.fundstransfer.exceptions.AccountNotFoundException;
import com.kshiitj.poc.fundstransfer.store.AccountStore;
import com.kshiitj.poc.fundstransfer.store.TransactionStore;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
/*
Service to Manage Account operations
Its major purpose if to persist the Accounts
Availabe as a Single instance throughout the application
 */
@Singleton
public class AccountService {
    private final AccountStore accountStore;
    private final TransactionStore transactionStore;
    @Inject
    private AccountService(AccountStore accountStore,TransactionStore transactionStore){
        this.accountStore=accountStore;
        this.transactionStore=transactionStore;
    }
    public Account getAccount(UUID accountId) throws AccountNotFoundException{
        return this.accountStore.getAccount(accountId);
    }
    public Account createAccount(BigDecimal initialBalance) throws AccountNotFoundException {
        Account account=new Account();
        this.accountStore.saveAccount(account);
        if (initialBalance.compareTo(BigDecimal.ZERO)>0){
            return deposit(account.getId(),initialBalance);
        }else{
            return account;
        }
    }
    public Account withdraw(UUID accountId,BigDecimal amount) throws AccountNotFoundException {
        Account account=this.accountStore.getAccount(accountId);
        account.withdraw(amount);
        this.accountStore.saveAccount(account);
        this.transactionStore.saveTransaction(
                Transaction.builder()
                        .id(UUID.randomUUID())
                        .accountId(accountId)
                        .stamp( new Date())
                        .type(TransactionType.DEBIT)
                        .amount(amount).build());
        return account;
    }

    public Account deposit(UUID accountId,BigDecimal amount) throws AccountNotFoundException {
        Account account=this.accountStore.getAccount(accountId);
        account.deposit(amount);
        this.accountStore.saveAccount(account);
        this.transactionStore.saveTransaction(
                Transaction.builder()
                        .id(UUID.randomUUID())
                        .accountId(accountId)
                        .stamp( new Date())
                        .type(TransactionType.CREDIT)
                        .amount(amount).build());
        return account;
    }
    public Optional<List<Transaction>> getAccountTransactions(UUID accountId){
        return Optional.of(this.transactionStore.getAllTransactionsForAnAccount(accountId));
    }
    public Optional<List<Account>> getAccounts() {
        return Optional.of(this.accountStore.getAllAccounts());
    }
}
