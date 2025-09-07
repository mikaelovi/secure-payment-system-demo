package com.demo.securepaymentsystem.model;

public class PaymentResponse {
    private String type;
    private String status;

    public PaymentResponse(String type, String status) {
        this.type = type;
        this.status = status;
    }

    public String getType() { return type; }
    public String getStatus() { return status; }
}
