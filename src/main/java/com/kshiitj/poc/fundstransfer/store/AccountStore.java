package com.kshiitj.poc.fundstransfer.store;

import com.kshiitj.poc.fundstransfer.domain.Account;
import com.kshiitj.poc.fundstransfer.exceptions.AccountNotFoundException;

import java.util.List;
import java.util.UUID;

public interface AccountStore {

    Account getAccount(UUID accountId) throws AccountNotFoundException;
    void saveAccount(Account account);
    Account removeAccount(UUID accountId);

    List<Account> getAllAccounts();
}
