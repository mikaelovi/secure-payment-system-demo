package com.demo.securepaymentsystem.service;

import com.demo.securepaymentsystem.model.PaymentRequest;
import com.demo.securepaymentsystem.model.PaymentResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PaymentServiceTest {
    private PaymentService paymentService;

    @BeforeEach
    void setUp() {
        paymentService = new PaymentService();
    }

    @Test
    void testCardPaymentSuccess() {
        PaymentRequest req = new PaymentRequest();
        req.setType("CARD");
        req.setAmount(100);

        PaymentResponse resp = paymentService.process("idempotent-1", req);
        assertNotNull(resp);
        assertEquals("CARD", resp.getType());
        assertTrue(resp.getStatus().equals("APPROVED") || resp.getStatus().equals("DECLINED"));
    }

    @Test
    void testBankPaymentSuccess() {
        PaymentRequest req = new PaymentRequest();
        req.setType("BANK");
        req.setAmount(200);

        PaymentResponse resp = paymentService.process("idempotent-2", req);
        assertEquals("BANK", resp.getType());
        assertEquals("APPROVED", resp.getStatus()); // Bank always approves if > 0
    }

    @Test
    void testWalletPaymentDecline() {
        PaymentRequest req = new PaymentRequest();
        req.setType("WALLET");
        req.setAmount(2000); // exceeds wallet limit

        PaymentResponse resp = paymentService.process("idempotent-3", req);
        assertEquals("WALLET", resp.getType());
        assertEquals("DECLINED", resp.getStatus());
    }

    @Test
    void testDuplicateRequestThrows() {
        PaymentRequest req = new PaymentRequest();
        req.setType("CARD");
        req.setAmount(100);

        paymentService.process("duplicate-key", req);
        assertThrows(IllegalStateException.class, () ->
                paymentService.process("duplicate-key", req));
    }

    @Test
    void testUnsupportedTypeThrows() {
        PaymentRequest req = new PaymentRequest();
        req.setType("CRYPTO");
        req.setAmount(50);

        assertThrows(IllegalArgumentException.class, () ->
                paymentService.process("idempotent-4", req));
    }
}