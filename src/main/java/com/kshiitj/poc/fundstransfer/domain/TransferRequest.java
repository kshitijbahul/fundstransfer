package com.kshiitj.poc.fundstransfer.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.joda.time.DateTime;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
public class TransferRequest {
    private UUID toAccountId;
    private UUID fromAccountId;
    private BigDecimal amount;
    private DateTime now;
    private String referenceId;
    public  TransferRequest(){}
}
