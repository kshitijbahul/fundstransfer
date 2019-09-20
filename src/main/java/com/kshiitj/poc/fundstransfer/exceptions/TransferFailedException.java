package com.kshiitj.poc.fundstransfer.exceptions;

public class TransferFailedException extends RuntimeException {
    public TransferFailedException(Throwable exception) {
        super(exception.getMessage(),exception);
    }
}
