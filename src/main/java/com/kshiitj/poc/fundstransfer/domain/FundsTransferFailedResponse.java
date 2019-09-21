package com.kshiitj.poc.fundstransfer.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FundsTransferFailedResponse {
    private FundsTransferResponse.Status error;
    private String message;
}
