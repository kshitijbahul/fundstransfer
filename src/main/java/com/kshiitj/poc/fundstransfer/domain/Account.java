package com.kshiitj.poc.fundstransfer.domain;

import com.kshiitj.poc.fundstransfer.exceptions.InsufficientBalanceException;
import lombok.AllArgsConstructor;
import lombok.NonNull;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
/*
Domain Object for Account, Handles its balances in a thread safe way
 */

@AllArgsConstructor
public class Account {
    @NonNull
    private UUID id;
    private BigDecimal balance;
    private Lock lock =new ReentrantLock();
    public Account(){
        this.id=UUID.randomUUID();
        this.balance=BigDecimal.ZERO;
    }
    public UUID getId() {
        return id;
    }

    public BigDecimal getBalance() {
        try {
            lock.tryLock(10, TimeUnit.MILLISECONDS);
                try{
                    return balance;
                }
                finally {
                    lock.unlock();
                }
        } catch (InterruptedException e) {
            throw new IllegalStateException();
        }
    }

    private boolean accountHasBalance(BigDecimal amount) {
        return this.getBalance().subtract(amount).compareTo(BigDecimal.ZERO) >=0;
    }


    public Account withdraw(BigDecimal amount) {
        if(accountHasBalance(amount)){
            if(this.lock.tryLock()){
                try{
                    this.balance=this.balance.subtract(amount);
                }finally {
                    this.lock.unlock();
                }
            }
        return this;
        }else{
            throw new InsufficientBalanceException(this.id,amount);
        }
    }
    public Account deposit(BigDecimal amount) {
        if(this.lock.tryLock()){
            try{
                this.balance=this.getBalance().add(amount);
            }finally {
                this.lock.unlock();
            }
        return this;
        }else {
            throw new IllegalStateException(String.format("Account %s is locked for operation.Credit not possible",this.id));
        }
    }
}
