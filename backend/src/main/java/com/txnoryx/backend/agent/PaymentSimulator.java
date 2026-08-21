package com.txnoryx.backend.agent;

import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class PaymentSimulator {

    private final Random random = new Random();

    public String retryPayment() {

        int result = random.nextInt(100);

        if (result < 75) {
            return "SUCCESS";
        }

        return "FAILED";
    }
}