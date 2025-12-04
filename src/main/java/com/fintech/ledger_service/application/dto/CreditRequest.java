package com.fintech.ledger_service.application.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreditRequest {

    @NotNull
    private String accountId;

    @NotNull
    @Min(1)
    private BigDecimal amount;

    @NotNull
    private String correlationId;
}
