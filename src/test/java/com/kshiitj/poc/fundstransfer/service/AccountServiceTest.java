package com.kshiitj.poc.fundstransfer.service;


import com.kshiitj.poc.fundstransfer.domain.Account;
import com.kshiitj.poc.fundstransfer.exceptions.AccountNotFoundException;
import com.kshiitj.poc.fundstransfer.exceptions.InsufficientBalanceException;
import com.kshiitj.poc.fundstransfer.store.InMemoryAccountStore;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;

import java.math.BigDecimal;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;


public class AccountServiceTest {

    static AccountService accountService;

    static Account newAccount;
    @BeforeClass
    public static void testBench(){
        accountService=new AccountService(new InMemoryAccountStore());
        newAccount=accountService.createAccount(BigDecimal.ZERO);
    }

    @Test
    public void test_creation(){

        BigDecimal initialBalance=BigDecimal.valueOf(100);
        Account account= accountService.createAccount(initialBalance);
        assertThat(account.getId().toString(),notNullValue());
        assertThat(account.getBalance(),equalTo(initialBalance));
    }

    @Test
    public void test_deposit() {
        BigDecimal startingBalance=newAccount.getBalance();
        Account updatedAccount=accountService.deposit(newAccount.getId(),BigDecimal.valueOf(100));
        assertThat(updatedAccount.getId(),equalTo(newAccount.getId()));
        assertThat(updatedAccount.getBalance(),equalTo(startingBalance.add(BigDecimal.valueOf(100))));
    }

    @Test
    public void test_withdrawal() {
        BigDecimal startingBalance=newAccount.getBalance();
        Account updatedAccount=accountService.withdraw(newAccount.getId(),BigDecimal.valueOf(50));
        assertThat(updatedAccount.getId(),equalTo(newAccount.getId()));
        assertThat(updatedAccount.getBalance(),equalTo(startingBalance.subtract(BigDecimal.valueOf(50))));
    }

    @Test(expected = InsufficientBalanceException.class)
    public void test_withdrawalNotPossible() {
        //Account updatedAccount=accountService.withdraw(newAccount.getId(),BigDecimal.valueOf(50));
        accountService.withdraw(newAccount.getId(),BigDecimal.valueOf(1000));
    }

    @Test(expected = AccountNotFoundException.class)
    public void test_AccountNotFound() {
        accountService.getAccount(UUID.randomUUID());
    }
    @Test(expected = AccountNotFoundException.class)
    public void test_withdrawFromNonExistingAccount()  {
        accountService.withdraw(UUID.randomUUID(),BigDecimal.valueOf(1000));
    }





}