# Secure Payment System – Demo

A simplified **payment processing system** built with **Spring Boot** as part of the Systegra (Interswitch) software engineering case study.

The system demonstrates key concepts of real-world payment systems:
- Multi-rail transaction support (**Cards, Bank Transfers, Wallets**)
- **Idempotency** (prevents duplicate charges)
- **Adapter pattern** for gateway integrations (mocked)
- **Basic request validation** (amount > 0, required fields)
- Clear separation of concerns (Controller → Service → Adapters → Store)

---

## 🚀 Getting Started

### Prerequisites
- Java 17+
- Maven (or use IntelliJ’s Maven wrapper)

### Run locally
```bash
  mvn spring-boot:run
```

### The API will be available at:
👉 http://localhost:8080/payments

### 📡 API Usage

**Endpoint**

`POST /payments`

**Headers**

`Content-Type: application/json`

`Idempotency-Key: <unique-key>`


**Request Body**

```{
{ 
      "type": "CARD",   // CARD | BANK | WALLET
      "amount": 100
}
```

**Example – Card Payment**
```bash
    curl -X POST http://localhost:8080/payments \
      -H "Content-Type: application/json" \
      -H "Idempotency-Key: tx123" \
      -d '{"type":"CARD","amount":150}'
```


✅ **Possible Responses**
```{ 
{
    "type": "CARD", 
    "status": "APPROVED" 
}
```

```{ 
{
    "type": "CARD", 
    "status": "DECLINED" 
}
```

**Example – Duplicate Request (Idempotency)**
```bash
    curl -X POST http://localhost:8080/payments \
      -H "Content-Type: application/json" \
      -H "Idempotency-Key: tx123" \
      -d '{"type":"CARD","amount":150}'
```

Response:

`"Duplicate request"
`