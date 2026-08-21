package com.txnoryx.backend.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionSummaryDTO(
        String transactionId,
        BigDecimal amount,
        String paymentMethod,
        String status,
        LocalDateTime createdAt
) {}