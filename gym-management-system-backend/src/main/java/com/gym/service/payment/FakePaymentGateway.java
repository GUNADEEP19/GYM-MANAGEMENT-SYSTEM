package com.gym.service.payment;

import org.springframework.stereotype.Service;

@Service
public class FakePaymentGateway implements PaymentGateway {

    @Override
    public PaymentGatewayResult validate(PaymentGatewayRequest request) {
        if (request.requiredAmount() <= 0) {
            return PaymentGatewayResult.declined("Invalid required amount");
        }
        if (request.providedAmount() == null || request.providedAmount() <= 0) {
            return PaymentGatewayResult.declined("Invalid payment amount");
        }
        if (request.providedAmount() + 1e-9 < request.requiredAmount()) {
            return PaymentGatewayResult.declined("Insufficient payment amount");
        }

        String method = request.method() == null ? "" : request.method().trim().toUpperCase();
        if (!(method.equals("UPI") || method.equals("CREDIT_CARD") || method.equals("DEBIT_CARD") || method.equals("CASH"))) {
            return PaymentGatewayResult.declined("Unsupported payment method");
        }

        // Demo hook: a discount code of FAIL forces a declined transaction.
        if (request.discountCode() != null && request.discountCode().trim().equalsIgnoreCase("FAIL")) {
            return PaymentGatewayResult.declined("Gateway declined transaction");
        }

        return PaymentGatewayResult.approvedResult();
    }
}
