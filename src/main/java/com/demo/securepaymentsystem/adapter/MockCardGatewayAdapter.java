package com.demo.securepaymentsystem.adapter;

import com.demo.securepaymentsystem.model.PaymentRequest;

public class MockCardGatewayAdapter implements GatewayAdapter {
    @Override
    public boolean authorize(PaymentRequest req) {
        // Basic fake success rate
        return req.getAmount() > 0 && Math.random() > 0.2;
    }
}
