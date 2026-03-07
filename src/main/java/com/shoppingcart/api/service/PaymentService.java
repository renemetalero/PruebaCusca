package com.shoppingcart.api.service;

import com.shoppingcart.api.dto.PaymentRegistrationRequest;
import com.shoppingcart.api.dto.PaymentRegistrationResponse;

import java.util.List;

public interface PaymentService {
    PaymentRegistrationResponse createPayment(PaymentRegistrationRequest request);
    PaymentRegistrationResponse updatePayment(PaymentRegistrationRequest request);
    void disablePayment(Long id);
    List<PaymentRegistrationResponse> getAllPayments();
    PaymentRegistrationResponse getPayment(Long id);
}
