package com.nastyzera.paymentservice.service;

import com.nastyzera.paymentservice.model.PaymentRequest;
import com.nastyzera.paymentservice.model.PaymentResponse;

public interface PaymentService {
    long doPayment(PaymentRequest paymentRequest);

    PaymentResponse getPaymentDetailsByOrderId(String orderId);
}
