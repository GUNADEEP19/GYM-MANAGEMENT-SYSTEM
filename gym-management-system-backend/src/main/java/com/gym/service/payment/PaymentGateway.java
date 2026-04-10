package com.gym.service.payment;

public interface PaymentGateway {
    PaymentGatewayResult validate(PaymentGatewayRequest request);
}
