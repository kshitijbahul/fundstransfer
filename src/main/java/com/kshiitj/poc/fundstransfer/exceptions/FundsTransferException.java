package com.kshiitj.poc.fundstransfer.exceptions;

import com.kshiitj.poc.fundstransfer.domain.FundsTransferResponse;
import lombok.Getter;

public class FundsTransferException extends RuntimeException {
    @Getter
    private FundsTransferResponse.Status status;
    public FundsTransferException(FundsTransferResponse.Status status, String message) {
        this(status,message,null);
    }
    public FundsTransferException(FundsTransferResponse.Status status, String message,Throwable throwable){
        super(message,throwable);
        this.status=status;
    }

}
