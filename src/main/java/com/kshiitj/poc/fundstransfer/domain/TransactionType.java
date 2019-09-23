package com.kshiitj.poc.fundstransfer.domain;

public enum TransactionType {
    CREDIT("C"),DEBIT("D");
    private String value;
    TransactionType(String value) {
        this.value=value;
    }


}
