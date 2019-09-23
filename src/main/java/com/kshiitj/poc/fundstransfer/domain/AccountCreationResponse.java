package com.kshiitj.poc.fundstransfer.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AccountCreationResponse {
    private UUID accountId;
}
