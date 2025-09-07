package com.demo.securepaymentsystem.adapter;

import com.demo.securepaymentsystem.model.PaymentRequest;

public interface GatewayAdapter {
    boolean authorize(PaymentRequest req);
}
