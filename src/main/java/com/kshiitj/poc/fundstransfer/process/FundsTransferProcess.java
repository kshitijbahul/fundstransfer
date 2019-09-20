package com.kshiitj.poc.fundstransfer.process;

import com.kshiitj.poc.fundstransfer.domain.Account;
import com.kshiitj.poc.fundstransfer.domain.TransferRequest;
import com.kshiitj.poc.fundstransfer.exceptions.AccountNotFoundException;
import com.kshiitj.poc.fundstransfer.exceptions.TransferFailedException;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static java.util.concurrent.CompletableFuture.*;

public class FundsTransferProcess {
    private ConcurrentHashMap<UUID,Account> accounts= new ConcurrentHashMap<>();
    public /*CompletableFuture<Account>*/void execute(TransferRequest transferRequest){

        transferRequest.verify();

    }
    private CompletableFuture<TransferRequest> verify(TransferRequest transferRequest){
        return CompletableFuture.supplyAsync(()->{
            transferRequest.verify();
            UUID source=transferRequest.getFromAccountId();
            UUID dest= transferRequest.getToAccountId();
            if (source == null){
                throw new AccountNotFoundException(transferRequest.getFromAccountId());
            }
            if (dest == null){
                throw new AccountNotFoundException(transferRequest.getToAccountId());
            }
            return transferRequest;
        });
    }
    private CompletableFuture<TransferRequest> withdraw(TransferRequest transferRequest){
        return supplyAsync(()->{
            Account source=accounts.get(transferRequest.getFromAccountId());
            source.withdraw(transferRequest.getAmount());
            return transferRequest;
        }).exceptionally(throwable -> {
            throw new TransferFailedException(throwable);
        });
    }
    private CompletableFuture<Account> deposit(TransferRequest transferRequest){
        return CompletableFuture.supplyAsync(()->{
            Account dest=accounts.get(transferRequest.getToAccountId());
            dest.deposit(transferRequest.getAmount());
            return dest;
        });
    }
    private CompletionStage<Account> finishTransfer(Account account){
        return completedFuture(account);
    }

}
