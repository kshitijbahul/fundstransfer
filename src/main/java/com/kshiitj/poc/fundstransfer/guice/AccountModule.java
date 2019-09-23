package com.kshiitj.poc.fundstransfer.guice;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Names;
import com.kshiitj.poc.fundstransfer.domain.Account;
import com.kshiitj.poc.fundstransfer.store.AccountStore;
import com.kshiitj.poc.fundstransfer.store.InMemoryAccountStore;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class AccountModule extends AbstractModule {
    @Override
    protected void configure() {
        ConcurrentHashMap<UUID, Account> accounts = new ConcurrentHashMap<>();
        bind(new TypeLiteral<Map<UUID, Account>>(){})
                .annotatedWith(Names.named("accountRepository"))
                .toInstance(accounts);
        bind(AccountStore.class).to(InMemoryAccountStore.class);
    }
}
