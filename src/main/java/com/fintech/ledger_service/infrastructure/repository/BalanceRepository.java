package com.fintech.ledger_service.infrastructure.repository;

import com.fintech.ledger_service.domain.entity.Balance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BalanceRepository extends JpaRepository<Balance, String> {

    Optional<Balance> findByAccountId(String accountId);
}
