package com.fintech.ledger_service.infrastructure.repository;

import com.fintech.ledger_service.domain.entity.LedgerEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LedgerEntryRepository extends JpaRepository<LedgerEntry, Long> {

    Optional<LedgerEntry> findByCorrelationId(String correlationId);

    List<LedgerEntry> findByAccountId(String accountId);
}
