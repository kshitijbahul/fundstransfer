package com.kshiitj.poc.fundstransfer.store;

import com.kshiitj.poc.fundstransfer.domain.Transaction;
import com.kshiitj.poc.fundstransfer.exceptions.AccountNotFoundException;
import com.kshiitj.poc.fundstransfer.exceptions.TransactionNotFoundException;

import java.util.List;
import java.util.UUID;

public interface TransactionStore {
    Transaction getTransaction(UUID transactionId) throws AccountNotFoundException, TransactionNotFoundException;
    void saveTransaction(Transaction account);
    List<Transaction> getAllTransactions();
    List<Transaction> getAllTransactionsForAnAccount(UUID accountId);
}
