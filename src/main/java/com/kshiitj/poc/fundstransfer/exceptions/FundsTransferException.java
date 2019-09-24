package com.kshiitj.poc.fundstransfer.exceptions;

import com.kshiitj.poc.fundstransfer.domain.TransferRequestStatus;
import lombok.Getter;

public class FundsTransferException extends RuntimeException {
    @Getter
    private TransferRequestStatus status;
    public FundsTransferException(TransferRequestStatus status, String message) {
        this(status,message,null);
    }
    public FundsTransferException(TransferRequestStatus status, String message, Throwable throwable){
        super(message,throwable);
        this.status=status;
    }

}
