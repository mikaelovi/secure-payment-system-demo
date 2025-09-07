package com.demo.securepaymentsystem.service;

import com.demo.securepaymentsystem.adapter.GatewayAdapter;
import com.demo.securepaymentsystem.adapter.MockBankGatewayAdapter;
import com.demo.securepaymentsystem.adapter.MockCardGatewayAdapter;
import com.demo.securepaymentsystem.adapter.MockWalletGatewayAdapter;
import com.demo.securepaymentsystem.model.PaymentRequest;
import com.demo.securepaymentsystem.model.PaymentResponse;
import com.demo.securepaymentsystem.store.IdempotencyStore;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PaymentService {
    private final IdempotencyStore store = new IdempotencyStore();

    public PaymentResponse process(String idempotencyKey, PaymentRequest req) {
        if (store.exists(idempotencyKey)) {
            log.warn("Duplicate request blocked for key={}", idempotencyKey);
            throw new IllegalStateException("Duplicate request");
        }
        store.save(idempotencyKey);

        log.info("Processing payment type={} amount={}", req.getType(), req.getAmount());


        GatewayAdapter adapter = getAdapter(req.getType());
        boolean success = adapter.authorize(req);

        PaymentResponse resp = new PaymentResponse(req.getType(), success ? "APPROVED" : "DECLINED");
        log.info("Payment result: type={} status={}", resp.getType(), resp.getStatus());

        return resp;
    }

    private GatewayAdapter getAdapter(String type) {
        return switch (type.toUpperCase()) {
            case "CARD" -> new MockCardGatewayAdapter();
            case "BANK" -> new MockBankGatewayAdapter();
            case "WALLET" -> new MockWalletGatewayAdapter();
            default -> {
                log.error("Unsupported payment type: {}", type);
                throw new IllegalArgumentException("Unsupported type: " + type);
            }
        };
    }
}
