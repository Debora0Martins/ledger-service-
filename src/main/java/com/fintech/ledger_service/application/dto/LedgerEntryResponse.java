package com.fintech.ledger_service.application.dto;

import com.fintech.ledger_service.domain.entity.LedgerEntry;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class LedgerEntryResponse {

    private Long id;
    private String accountId; // ← CORREÇÃO AQUI
    private BigDecimal amount;
    private String type;
    private String correlationId;
    private LocalDateTime createdAt;

    public static LedgerEntryResponse fromEntity(LedgerEntry entry) {
        return LedgerEntryResponse.builder()
                .id(entry.getId())
                .accountId(String.valueOf(entry.getAccountId()))  // ← CORREÇÃO AQUI
                .amount(entry.getAmount())
                .type(entry.getType().name())
                .correlationId(entry.getCorrelationId())
                .createdAt(entry.getCreatedAt())
                .build();
    }
}
