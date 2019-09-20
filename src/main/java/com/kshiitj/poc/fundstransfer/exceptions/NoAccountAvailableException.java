package com.kshiitj.poc.fundstransfer.exceptions;

public class NoAccountAvailableException extends RuntimeException{
    public NoAccountAvailableException(){
        super("No Accounts in the System yet. Use POST request {\"initialBalance\":10} to create one.");
    }
    NoAccountAvailableException(String message,Throwable  exp){
        super(message,exp);
    }
}
