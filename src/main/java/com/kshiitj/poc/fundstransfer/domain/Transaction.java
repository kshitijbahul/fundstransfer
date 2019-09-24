package com.kshiitj.poc.fundstransfer.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;
/*
Domain Object for Transaction
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Transaction {
    private UUID id;
    private UUID  accountId;
    private Date stamp;
    private TransactionType type;
    private BigDecimal amount;
}
