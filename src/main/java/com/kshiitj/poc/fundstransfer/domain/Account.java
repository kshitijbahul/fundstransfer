package com.kshiitj.poc.fundstransfer.domain;

import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
@RequiredArgsConstructor()
@NoArgsConstructor
public class Account {
    @NonNull
    private UUID id;

    private BigDecimal balance=BigDecimal.ZERO;
}
