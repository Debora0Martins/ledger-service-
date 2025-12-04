package com.fintech.ledger_service.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "balances")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Balance {

    @Id
    private String accountId;  // 🔥 Agora é STRING em todo o projeto!

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal currentBalance;
}
