package com.kshiitj.poc.fundstransfer.domain;

import lombok.*;
import org.joda.time.DateTime;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
public class TransferRequest {
    private UUID requestId;
    private UUID toAccountId;
    private UUID fromAccountId;
    private BigDecimal amount;

    public TransferRequest(UUID fromAccountId,UUID toAccountId,BigDecimal amount){
        this.requestId=UUID.randomUUID();
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
