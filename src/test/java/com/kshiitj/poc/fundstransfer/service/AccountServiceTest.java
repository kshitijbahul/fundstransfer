package com.kshiitj.poc.fundstransfer.service;


import com.kshiitj.poc.fundstransfer.FundsTransferApplication;
import com.kshiitj.poc.fundstransfer.FundsTransferConfiguration;
import com.kshiitj.poc.fundstransfer.domain.Account;
import com.kshiitj.poc.fundstransfer.domain.Transaction;
import com.kshiitj.poc.fundstransfer.exceptions.AccountNotFoundException;
import com.kshiitj.poc.fundstransfer.exceptions.InsufficientBalanceException;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import ru.vyarus.dropwizard.guice.test.GuiceyAppRule;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;


public class AccountServiceTest {
    public static AccountService accountService;
    @ClassRule
    public static GuiceyAppRule<FundsTransferConfiguration> RULE = new GuiceyAppRule<>(FundsTransferApplication.class, null);

    static Account newAccount;
    @BeforeClass
    public static void testBench(){
        accountService=RULE.getBean(AccountService.class);
        try {
            newAccount=RULE.getBean(AccountService.class).createAccount(BigDecimal.ZERO);
        } catch (AccountNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test_creation(){

        BigDecimal initialBalance=BigDecimal.valueOf(100);
        Account account= null;
        try {
            account = accountService.createAccount(initialBalance);
        } catch (AccountNotFoundException e) {
            e.printStackTrace();
        }
        assertThat(account.getId().toString(),notNullValue());
        assertThat(account.getBalance(),equalTo(initialBalance));
        Optional<List<Transaction>> transactionOptional=accountService.getAccountTransactions(account.getId());
        assertThat(transactionOptional.isPresent(),equalTo(Boolean.TRUE));
        assertThat(transactionOptional.get(),hasSize(1));
    }

    @Test
    public void test_deposit() throws AccountNotFoundException {
        BigDecimal startingBalance=newAccount.getBalance();
        Account updatedAccount=accountService.deposit(newAccount.getId(),BigDecimal.valueOf(100));
        assertThat(updatedAccount.getId(),equalTo(newAccount.getId()));
        assertThat(updatedAccount.getBalance(),equalTo(startingBalance.add(BigDecimal.valueOf(100))));
        Optional<List<Transaction>> transactionOptional=accountService.getAccountTransactions(updatedAccount.getId());
        assertThat(transactionOptional.isPresent(),equalTo(Boolean.TRUE));
    }

    @Test
    public void test_withdrawal() throws AccountNotFoundException {
        BigDecimal startingBalance=newAccount.getBalance();
        Account updatedAccount=accountService.withdraw(newAccount.getId(),BigDecimal.valueOf(50));
        assertThat(updatedAccount.getId(),equalTo(newAccount.getId()));
        assertThat(updatedAccount.getBalance(),equalTo(startingBalance.subtract(BigDecimal.valueOf(50))));
        Optional<List<Transaction>> transactionOptional=accountService.getAccountTransactions(updatedAccount.getId());
        assertThat(transactionOptional.isPresent(),equalTo(Boolean.TRUE));
    }

    @Test(expected = InsufficientBalanceException.class)
    public void test_withdrawalNotPossible() throws AccountNotFoundException {
        //Account updatedAccount=accountService.withdraw(newAccount.getId(),BigDecimal.valueOf(50));
        accountService.withdraw(newAccount.getId(),BigDecimal.valueOf(1000));
    }

    @Test(expected = AccountNotFoundException.class)
    public void test_AccountNotFound() throws AccountNotFoundException {
        accountService.getAccount(UUID.randomUUID());
    }
    @Test(expected = AccountNotFoundException.class)
    public void test_withdrawFromNonExistingAccount() throws AccountNotFoundException {
        accountService.withdraw(UUID.randomUUID(),BigDecimal.valueOf(1000));
    }
}