package com.fintech.ledger_service.domain.service;

import com.fintech.ledger_service.application.dto.BalanceResponse;
import com.fintech.ledger_service.domain.entity.Balance;
import com.fintech.ledger_service.domain.entity.LedgerEntry;
import com.fintech.ledger_service.domain.entity.LedgerEntry.LedgerType;
import com.fintech.ledger_service.domain.exception.DomainException;
import com.fintech.ledger_service.infrastructure.kafka.LedgerProducer;
import com.fintech.ledger_service.infrastructure.repository.BalanceRepository;
import com.fintech.ledger_service.infrastructure.repository.LedgerEntryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class LedgerService {

    private final LedgerEntryRepository ledgerEntryRepository;
    private final BalanceRepository balanceRepository;
    private final LedgerProducer ledgerProducer;

    private void validateIdempotency(String correlationId) {
        ledgerEntryRepository.findByCorrelationId(correlationId)
                .ifPresent(e -> {
                    throw new DomainException("Transação duplicada: correlationId já processado.");
                });
    }

    // ➖ DÉBITO
    @Transactional
    public LedgerEntry debit(String accountId, BigDecimal amount, String correlationId) {

        validateIdempotency(correlationId);

        Balance balance = balanceRepository.findByAccountId(accountId)
                .orElse(Balance.builder()
                        .accountId(accountId)
                        .currentBalance(BigDecimal.ZERO)
                        .build());

        if (balance.getCurrentBalance().compareTo(amount) < 0) {
            throw new DomainException("Saldo insuficiente para débito.");
        }

        balance.setCurrentBalance(balance.getCurrentBalance().subtract(amount));
        balanceRepository.save(balance);

        LedgerEntry entry = LedgerEntry.builder()
                .accountId(accountId)
                .type(LedgerType.DEBIT)
                .amount(amount)
                .correlationId(correlationId)
                .build();

        LedgerEntry saved = ledgerEntryRepository.save(entry);

        // 🔥 ENVIA APENAS STRING PARA O KAFKA
        ledgerProducer.send(accountId, saved.getId().toString());

        log.info("Débito registrado: accountId={}, amount={}, correlationId={}",
                accountId, amount, correlationId);

        return saved;
    }

    // ➕ CRÉDITO
    @Transactional
    public LedgerEntry credit(String accountId, BigDecimal amount, String correlationId) {

        validateIdempotency(correlationId);

        Balance balance = balanceRepository.findByAccountId(accountId)
                .orElse(Balance.builder()
                        .accountId(accountId)
                        .currentBalance(BigDecimal.ZERO)
                        .build());

        balance.setCurrentBalance(balance.getCurrentBalance().add(amount));
        balanceRepository.save(balance);

        LedgerEntry entry = LedgerEntry.builder()
                .accountId(accountId)
                .type(LedgerType.CREDIT)
                .amount(amount)
                .correlationId(correlationId)
                .build();

        LedgerEntry saved = ledgerEntryRepository.save(entry);

        ledgerProducer.send(accountId, saved.getId().toString());

        log.info("Crédito registrado: accountId={}, amount={}, correlationId={}",
                accountId, amount, correlationId);

        return saved;
    }

    // 🔍 LISTAR LANÇAMENTOS
    public List<LedgerEntry> findEntries(String accountId) {
        return ledgerEntryRepository.findByAccountId(accountId);
    }

    // 🔍 OBTER SALDO
    public BalanceResponse getBalance(String accountId) {

        Balance balance = balanceRepository.findByAccountId(accountId)
                .orElse(Balance.builder()
                        .accountId(accountId)
                        .currentBalance(BigDecimal.ZERO)
                        .build());

        return new BalanceResponse(
                balance.getAccountId(),
                balance.getCurrentBalance()
        );
    }
}
