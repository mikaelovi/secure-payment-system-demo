package com.demo.securepaymentsystem.adapter;

import com.demo.securepaymentsystem.model.PaymentRequest;

public class MockBankGatewayAdapter implements GatewayAdapter {
    @Override
    public boolean authorize(PaymentRequest req) {
        // Simulate slower but reliable processing
        return req.getAmount() > 0;
    }
}
