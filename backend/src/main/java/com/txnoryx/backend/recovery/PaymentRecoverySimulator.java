package com.txnoryx.backend.recovery;

import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class PaymentRecoverySimulator {

    private final Random random = new Random();

    public boolean retry() {

        return random.nextInt(100) < 80;
    }

    public boolean alternativeRoute() {

        return random.nextInt(100) < 65;
    }
}