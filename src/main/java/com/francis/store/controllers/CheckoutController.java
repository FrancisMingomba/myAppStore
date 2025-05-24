package com.francis.store.controllers;

import com.francis.store.dtos.CheckoutRequest;
import com.francis.store.dtos.CheckoutResponse;
import com.francis.store.dtos.ErrorDto;
import com.francis.store.exceptions.CartEmptyException;
import com.francis.store.exceptions.CartNotFoundException;
import com.francis.store.exceptions.PaymentException;
import com.francis.store.repositories.OrderRepository;
import com.francis.store.services.CheckoutService;
import com.francis.store.services.WebhookRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/checkout")
public class CheckoutController {

    private final CheckoutService checkoutService;
    private final OrderRepository orderRepository;


    @PostMapping
    public CheckoutResponse checkout(@Valid @RequestBody CheckoutRequest request) {

            return checkoutService.checkout(request);
    }

    @PostMapping("/webhook")
    public void handleWebhook(
            @RequestHeader Map<String, String> headers,
            @RequestBody String payload
    ) {
       checkoutService.handleWebhookEvent(new WebhookRequest(headers, payload));
    }



    @ExceptionHandler(PaymentException.class)
    public ResponseEntity<?> handlePaymentException() {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorDto("Error creating a checkout session"));
    }

    @ExceptionHandler({CartNotFoundException.class, CartEmptyException.class})
    public ResponseEntity<ErrorDto> handleException(Exception ex) {
        return ResponseEntity.badRequest().body(new ErrorDto(ex.getMessage()));
    }


}
