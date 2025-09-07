package com.demo.securepaymentsystem.adapter;

import com.demo.securepaymentsystem.model.PaymentRequest;

public class MockWalletGatewayAdapter implements GatewayAdapter {
    @Override
    public boolean authorize(PaymentRequest req) {
        // Simulate wallet limit check
        return req.getAmount() <= 1000;
    }
}
