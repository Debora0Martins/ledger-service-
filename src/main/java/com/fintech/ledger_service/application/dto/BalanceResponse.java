package com.fintech.ledger_service.application.dto;

import com.fintech.ledger_service.domain.entity.Balance;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
public class BalanceResponse {

    private String accountId;
    private BigDecimal currentBalance;

    public static BalanceResponse fromEntity(Balance balance) {
        return BalanceResponse.builder()
                .accountId(String.valueOf(balance.getAccountId()))
                .currentBalance(balance.getCurrentBalance())
                .build();
    }
}
