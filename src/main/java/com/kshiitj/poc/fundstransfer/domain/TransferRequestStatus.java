package com.kshiitj.poc.fundstransfer.domain;
/*
Enum to Manage The Possible statues of A transfer Request
 */
public enum TransferRequestStatus {
    INITIATED("Request Initiated"),
    INVALID_TRANSFER_REQUEST("Invalid Transfer Request"),
    SUCCESS("Transfer Successful"),
    DEBIT_FAILED("Source account debit failed"),
    CREDIT_FAILED("Could not credit account"),
    REVERSAL_FAILED("Could not reverse failed Credit");
    String message;
    TransferRequestStatus(String message){
        this.message=message;
    }
}
