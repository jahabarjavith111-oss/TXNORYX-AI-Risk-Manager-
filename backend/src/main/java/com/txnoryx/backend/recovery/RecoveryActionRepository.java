package com.txnoryx.backend.recovery;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RecoveryActionRepository
        extends JpaRepository<RecoveryAction, Long> {
}