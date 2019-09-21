package com.kshiitj.poc.fundstransfer.exceptions;

import java.util.UUID;

public class AccountNotFoundException extends Throwable {

    private int code;
    public AccountNotFoundException(UUID accountId){
        this(500,accountId);
    }
    AccountNotFoundException(int code,UUID accountId){
        this(code,String.format("Account  %s does not exist.",accountId.toString()));
    }
    AccountNotFoundException(int code,String message){
        this(code,message,null);
    }
    AccountNotFoundException(int code,String message,Throwable throwable){
        super(message,throwable);
        this.code=code;
    }
    public int getCode(){
        return this.code;
    }
}
