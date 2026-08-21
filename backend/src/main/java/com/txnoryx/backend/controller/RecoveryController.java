package com.txnoryx.backend.controller;

import com.txnoryx.backend.recovery.RecoveryResult;
import com.txnoryx.backend.recovery.RecoveryService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/recovery")
public class RecoveryController {

    private final RecoveryService recoveryService;

    public RecoveryController(RecoveryService recoveryService) {
        this.recoveryService = recoveryService;
    }

    @PostMapping(value = "/{transactionId}", produces = "application/json")
    public RecoveryResult recover(@PathVariable String transactionId) {
        return recoveryService.recover(transactionId);
    }

    @GetMapping(value = "/{transactionId}", produces = "application/json")
    public RecoveryResult getRecovery(@PathVariable String transactionId) {
        return recoveryService.recover(transactionId);
    }
}