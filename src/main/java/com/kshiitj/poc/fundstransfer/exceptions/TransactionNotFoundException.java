package com.kshiitj.poc.fundstransfer.exceptions;

import java.util.UUID;

public class TransactionNotFoundException extends Throwable {

    private int code;
    public TransactionNotFoundException(UUID transactionId){
        this(500,transactionId);
    }
    TransactionNotFoundException(int code, UUID transactionId){
        this(code,String.format("Transaction  %s does not exist.",transactionId.toString()));
    }
    TransactionNotFoundException(int code, String message){
        this(code,message,null);
    }
    TransactionNotFoundException(int code, String message, Throwable throwable){
        super(message,throwable);
        this.code=code;
    }
    public int getCode(){
        return this.code;
    }
}
