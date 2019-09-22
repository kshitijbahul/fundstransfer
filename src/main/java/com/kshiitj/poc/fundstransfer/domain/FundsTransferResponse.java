package com.kshiitj.poc.fundstransfer.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FundsTransferResponse {
    public enum Status{
        INVALID_TRANSFER_REQUEST("Invalid Transfer Request"),
        SUCCESS("Transfer Successful"),
        DEBIT_FAILED("Source account debit failed"),
        CREDIT_FAILED("Could not credit account"),
        REVERSAL_FAILED("Could not reverse failed Credit");
        String message;
        Status(String message){
            this.message=message;
        }
    }
    private UUID transferId;
    private Status status;
}
