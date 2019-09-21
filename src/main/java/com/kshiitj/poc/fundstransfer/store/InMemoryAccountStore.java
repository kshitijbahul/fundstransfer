package com.kshiitj.poc.fundstransfer.store;

import com.kshiitj.poc.fundstransfer.domain.Account;
import com.kshiitj.poc.fundstransfer.exceptions.AccountNotFoundException;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.util.*;
import java.util.stream.Collectors;

@Singleton
public class InMemoryAccountStore implements AccountStore {
    private final Map<UUID,Account> accounts;
    @Inject
    public InMemoryAccountStore(@Named("accountRepository") Map<UUID,Account> accounts){
        this.accounts=accounts;
    }
    @Override
    public Account getAccount(UUID accountId) throws AccountNotFoundException {
        if (this.accounts.containsKey(accountId)){
            return this.accounts.get(accountId);
        }else{
            throw new AccountNotFoundException(accountId);
        }

    }

    @Override
    public void saveAccount(Account account) {
        this.accounts.put(account.getId(),account);
    }

    @Override
    public Account removeAccount(UUID accountId) {
        return this.accounts.remove(accountId);
    }

    @Override
    public List<Account> getAllAccounts() {
        return this.accounts.values().parallelStream().collect(Collectors.toList());
    }


}
