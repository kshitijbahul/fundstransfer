package com.kshiitj.poc.fundstransfer.domain;
/*
ENUM to indicate transaction type for transaction log
 */
public enum TransactionType {
    CREDIT("C"),DEBIT("D");
    private String value;
    TransactionType(String value) {
        this.value=value;
    }


}
