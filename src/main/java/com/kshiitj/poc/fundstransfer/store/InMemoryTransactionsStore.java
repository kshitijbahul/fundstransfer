package com.kshiitj.poc.fundstransfer.store;

import com.kshiitj.poc.fundstransfer.domain.Transaction;
import com.kshiitj.poc.fundstransfer.exceptions.TransactionNotFoundException;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Singleton
public class InMemoryTransactionsStore implements TransactionStore {
    private final LinkedList<Transaction> transactions;

    @Inject
    InMemoryTransactionsStore(@Named("transactionRepository") LinkedList<Transaction> transactions){
        this.transactions=transactions;
    }
    @Override
    public Transaction getTransaction(UUID transactionId) throws TransactionNotFoundException {
        return this.transactions.parallelStream().filter(transaction-> transaction.getId().equals(transactionId)).findFirst().orElseThrow(()->new TransactionNotFoundException(transactionId));

    }

    @Override
    public void saveTransaction(Transaction transaction) {
        this.transactions.add(transaction);
    }

    @Override
    public List<Transaction> getAllTransactions() {
        return  (this.transactions.parallelStream().collect(Collectors.toList()));
    }
    @Override
    public List<Transaction> getAllTransactionsForAnAccount(UUID accountId){
        return this.transactions.parallelStream().filter((eachTransaction)->{return eachTransaction.getAccountId().equals(accountId);}).collect(Collectors.toList());
    }
}
