package com.demo.securepaymentsystem.service;

import com.demo.securepaymentsystem.adapter.GatewayAdapter;
import com.demo.securepaymentsystem.adapter.MockBankGatewayAdapter;
import com.demo.securepaymentsystem.adapter.MockCardGatewayAdapter;
import com.demo.securepaymentsystem.adapter.MockWalletGatewayAdapter;
import com.demo.securepaymentsystem.model.PaymentRequest;
import com.demo.securepaymentsystem.model.PaymentResponse;
import com.demo.securepaymentsystem.store.IdempotencyStore;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {
    private final IdempotencyStore store = new IdempotencyStore();

    public PaymentResponse process(String idempotencyKey, PaymentRequest req) {
        if (store.exists(idempotencyKey)) {
            throw new IllegalStateException("Duplicate request");
        }
        store.save(idempotencyKey);

        GatewayAdapter adapter = getAdapter(req.getType());
        boolean success = adapter.authorize(req);

        return new PaymentResponse(req.getType(), success ? "APPROVED" : "DECLINED");
    }

    private GatewayAdapter getAdapter(String type) {
        return switch (type.toUpperCase()) {
            case "CARD" -> new MockCardGatewayAdapter();
            case "BANK" -> new MockBankGatewayAdapter();
            case "WALLET" -> new MockWalletGatewayAdapter();
            default -> throw new IllegalArgumentException("Unsupported type: " + type);
        };
    }
}
