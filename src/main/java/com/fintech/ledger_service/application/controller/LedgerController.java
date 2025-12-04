package com.fintech.ledger_service.application.controller;

import com.fintech.ledger_service.application.dto.*;
import com.fintech.ledger_service.domain.entity.LedgerEntry;
import com.fintech.ledger_service.domain.service.LedgerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/ledger")
@RequiredArgsConstructor
public class LedgerController {

    private final LedgerService ledgerService;

    @PostMapping("/debit")
    public ResponseEntity<LedgerEntryResponse> debit(@Valid @RequestBody DebitRequest request) {
        LedgerEntry entry = ledgerService.debit(
                request.getAccountId(),
                request.getAmount(),
                request.getCorrelationId()
        );
        return ResponseEntity.ok(LedgerEntryResponse.fromEntity(entry));
    }

    @PostMapping("/credit")
    public ResponseEntity<LedgerEntryResponse> credit(@Valid @RequestBody CreditRequest request) {
        LedgerEntry entry = ledgerService.credit(
                request.getAccountId(),
                request.getAmount(),
                request.getCorrelationId()
        );
        return ResponseEntity.ok(LedgerEntryResponse.fromEntity(entry));
    }

    @GetMapping("/{accountId}/balance")
    public ResponseEntity<BalanceResponse> getBalance(@PathVariable String accountId) {
        return ResponseEntity.ok(ledgerService.getBalance(accountId));
    }

    @GetMapping("/{accountId}/entries")
    public ResponseEntity<List<LedgerEntryResponse>> getEntries(@PathVariable String accountId) {

        List<LedgerEntry> entries = ledgerService.findEntries(accountId);

        List<LedgerEntryResponse> response = entries.stream()
                .map(LedgerEntryResponse::fromEntity)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/entry/{id}")
    public ResponseEntity<LedgerEntryResponse> getEntryById(@PathVariable Long id) {

        LedgerEntry entry = ledgerService.findEntries(null)
                .stream()
                .filter(e -> e.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Lançamento não encontrado"));

        return ResponseEntity.ok(LedgerEntryResponse.fromEntity(entry));
    }
}
