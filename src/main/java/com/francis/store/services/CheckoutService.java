package com.francis.store.services;

import com.francis.store.dtos.CheckoutRequest;
import com.francis.store.dtos.CheckoutResponse;
import com.francis.store.entities.Order;
import com.francis.store.exceptions.CartEmptyException;
import com.francis.store.exceptions.CartNotFoundException;
import com.francis.store.exceptions.PaymentException;
import com.francis.store.repositories.CartRepository;
import com.francis.store.repositories.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CheckoutService {
    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final AuthService authService;
    private final CartService cartService;
    private final PaymentGateway paymentGateway;



    @Transactional
    public CheckoutResponse checkout(CheckoutRequest request){
        var cart = cartRepository.findById(request.getCartId()).orElse(null);
        if (cart == null) {
           throw new CartNotFoundException();
        }
        if (cart.isEmpty()) {
          throw new CartEmptyException();
        }

        var order =  Order.fromCart(cart, authService.getCurrentUser());

        orderRepository.save(order);

        try {
            //Create a checkout session
          var session =  paymentGateway.createCheckoutSession(order);

            cartService.clearCart(cart.getId());

            return new CheckoutResponse(order.getId(), session.getCheckoutUrl());

        } catch (PaymentException ex) {
            orderRepository.delete(order);
            throw ex;

        }

    }

    public void handleWebhookEvent(WebhookRequest request) {
        paymentGateway
                .parseWebhookRequest(request)
        .ifPresent( paymentResult -> {
            var order = orderRepository.findById(paymentResult.getOrderId()).orElseThrow();
            order.setStatus(paymentResult.getPaymentStatus());
            orderRepository.save(order);
        } );

    }
}
