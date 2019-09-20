package com.kshiitj.poc.fundstransfer.exceptions;

import java.math.BigDecimal;
import java.util.UUID;

public class InsufficientBalanceException extends RuntimeException {
    private int code;
    public InsufficientBalanceException(UUID accountId, BigDecimal amount){
        this(500,accountId,amount);
    }
    InsufficientBalanceException(int code,UUID accountId,BigDecimal amount){
        this(code,String.format("Insufficient Balance in Account %s to withdraw %s",accountId.toString(),amount.toString()));
    }
    InsufficientBalanceException(int code,String message){
        this(code,message,null);
    }
    InsufficientBalanceException(int code,String message,Throwable throwable){
        super(message,throwable);
        this.code=code;
    }
    public int getCode(){
        return this.code;
    }

}
