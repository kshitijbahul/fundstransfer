package com.kshiitj.poc.fundstransfer.store;

import com.kshiitj.poc.fundstransfer.domain.Account;
import com.kshiitj.poc.fundstransfer.exceptions.AccountNotFoundException;

import javax.inject.Singleton;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
@Singleton
public class InMemoryAccountStore implements AccountStore {
    private ConcurrentHashMap<UUID,Account> accounts= new ConcurrentHashMap<>();
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
        return Collections.list(this.accounts.elements());
    }


}
