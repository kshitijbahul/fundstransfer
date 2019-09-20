package com.kshiitj.poc.fundstransfer.domain;

import com.kshiitj.poc.fundstransfer.exceptions.InsufficientBalanceException;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


@AllArgsConstructor
//@RequiredArgsConstructor()
@NoArgsConstructor
public class Account {
    @NonNull
    private UUID id;
    private BigDecimal balance=BigDecimal.ZERO;
    //private ReentrantReadWriteLock reentrantLock=new ReentrantReadWriteLock();
    private Lock lock =new ReentrantLock();
    //private Lock writeLock=reentrantLock.writeLock();

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

    public Account (BigDecimal initialBalance) {
        this.id=UUID.randomUUID();
        this.balance=initialBalance;
    }
    public void withdraw(BigDecimal amount) {
        if(accountHasBalance(amount)){
            if(this.lock.tryLock()){
                try{
                    this.balance=this.balance.subtract(amount);
                }finally {
                    this.lock.unlock();
                }
            }
        }else{
            throw new InsufficientBalanceException(this.id,this.balance);
        }
    }
    public void deposit(BigDecimal amount) {
        if(this.lock.tryLock()){
            try{
                this.balance=this.getBalance().add(amount);
            }finally {
                this.lock.unlock();
            }

        }else {
            new IllegalStateException(String.format("Account %s is locked for operation.Debit not possible",this.id));
        }

    }
}