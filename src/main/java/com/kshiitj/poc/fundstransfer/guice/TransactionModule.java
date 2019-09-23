package com.kshiitj.poc.fundstransfer.guice;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Names;
import com.kshiitj.poc.fundstransfer.domain.Transaction;
import com.kshiitj.poc.fundstransfer.store.InMemoryTransactionsStore;
import com.kshiitj.poc.fundstransfer.store.TransactionStore;

import java.util.LinkedList;

public class TransactionModule extends AbstractModule {
    @Override
    protected void configure() {
        LinkedList<Transaction> transactions = new LinkedList<>();
        bind(new TypeLiteral<LinkedList<Transaction>>(){})
                .annotatedWith(Names.named("transactionRepository"))
                .toInstance(transactions);
        bind(TransactionStore.class).to(InMemoryTransactionsStore.class);
    }
}
