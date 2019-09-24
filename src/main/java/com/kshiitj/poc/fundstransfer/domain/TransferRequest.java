package com.kshiitj.poc.fundstransfer.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
public class TransferRequest {
    private UUID requestId=UUID.randomUUID();
    private UUID toAccountId;
    private UUID fromAccountId;
    private BigDecimal amount;
    private TransferRequestStatus transferRequestStatus=TransferRequestStatus.INITIATED;

    public TransferRequest(UUID fromAccountId,UUID toAccountId,BigDecimal amount){
        this.toAccountId=toAccountId;
        this.fromAccountId=fromAccountId;
        this.amount=amount;
    }
    public void verify() {
        Set errors= new HashSet<String>();
        if (this.fromAccountId == null){
            errors.add("Invalid Source Account");
        }
        if (this.toAccountId == null){
            errors.add("Invalid Destination Account");
        }
        if (this.amount == null || this.amount.compareTo(BigDecimal.ZERO)<=0){
            errors.add("Invalid Transfer Amount");
        }
        if(!errors.isEmpty()){
            throw new IllegalArgumentException(String.join(",",errors));
        }
    }
}
