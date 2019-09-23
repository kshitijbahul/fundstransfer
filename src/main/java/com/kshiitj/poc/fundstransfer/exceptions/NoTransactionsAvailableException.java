package com.kshiitj.poc.fundstransfer.exceptions;

public class NoTransactionsAvailableException extends RuntimeException{
    public NoTransactionsAvailableException(){
        super("No transactions in the System yet. Use the system to get transactions.");
    }
    NoTransactionsAvailableException(String message, Throwable  exp){
        super(message,exp);
    }
}
