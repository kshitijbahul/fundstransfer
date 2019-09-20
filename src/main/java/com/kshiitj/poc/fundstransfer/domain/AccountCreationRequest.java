package com.kshiitj.poc.fundstransfer.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountCreationRequest {
    private BigDecimal initialBalance;
    public void verify(){
        Set errors = new HashSet<String>();
        if (this.initialBalance.compareTo(BigDecimal.ZERO)<0){
            errors.add("Initial balance cannot be negitive");
        }
        if (!errors.isEmpty()){
            throw  new IllegalArgumentException(String.join(",",errors));
        }
    }
}
