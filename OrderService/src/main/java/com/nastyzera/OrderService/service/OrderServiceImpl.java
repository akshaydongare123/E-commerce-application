package com.nastyzera.OrderService.service;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nastyzera.OrderService.entity.Order;
import com.nastyzera.OrderService.exception.CustomException;
import com.nastyzera.OrderService.external.client.PaymentService;
import com.nastyzera.OrderService.external.client.ProductService;
import com.nastyzera.OrderService.external.request.PaymentRequest;
import com.nastyzera.OrderService.external.response.PaymentResponse;
import com.nastyzera.OrderService.external.response.ProductResponse;
import com.nastyzera.OrderService.model.OrderRequest;
import com.nastyzera.OrderService.model.OrderResponse;
import com.nastyzera.OrderService.repository.OrderRepository;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private PaymentService paymentService;

    // @Autowired
    // private ProductService productService;

    // @Autowired
    // private PaymentService paymentService;


    @Override
    public long placeOrder(OrderRequest orderRequest) {
        log.info("Placing Order Request: {}", orderRequest);
        //Order Entity -> Save the data with Status Order Created
        //Product Service - Block Products (Reduce the Quantity)
        //Payment Service -> Payments -> Success-> COMPLETE, Else
        //CANCELLED

        productService.reduceQuantity(orderRequest.getProductId(),orderRequest.getQuantity());



        log.info("Creating Order with Status CREATED");
        
        Order order = Order.builder()
                .amount(orderRequest.getTotalAmount())
                .orderStatus("CREATED")
                .productId(orderRequest.getProductId())
                .orderDate(Instant.now())
                .quantity(orderRequest.getQuantity())
                .build();


        order = orderRepository.save(order);
        
        log.info("Calling Payment Service to complete the Order");
        log.info("Calling Payment Service to complete the payment");
        PaymentRequest paymentRequest
                = PaymentRequest.builder()
                .orderId(order.getId())
                .paymentMode(orderRequest.getPaymentMode())
                .amount(orderRequest.getTotalAmount())
                .build();

        String orderStatus = null;
        try {
            paymentService.doPayment(paymentRequest);
            log.info("Payment done Successfully. Changing the Oder status to PLACED");
            orderStatus = "PLACED";
        } catch (Exception e) {
            log.error("Error occurred in payment. Changing order status to PAYMENT_FAILED");
            orderStatus = "PAYMENT_FAILED";
        }

        order.setOrderStatus(orderStatus);
        orderRepository.save(order);

        log.info("Order Places successfully with Order Id: {}", order.getId());
        return order.getId();

    }

    @Override
    public OrderResponse getOrderDetails(long orderId) {
        log.info("Get order details for Order Id : {}", orderId);

        Order order
                = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException("Order not found for the order Id:" + orderId,
                        "NOT_FOUND",
                        404));

        log.info("Invoking Product service to fetch the product for id: {}", order.getProductId());
        ProductResponse productResponse
                = productService.getProductById(order.getProductId()).getBody();
                
        log.info("Getting payment information form the payment Service");
        PaymentResponse paymentResponse
                = paymentService.getPaymentDetailsByOrderId(String.valueOf(orderId)).getBody();

        OrderResponse.ProductDetails productDetails
                = OrderResponse.ProductDetails
                .builder()
                .productName(productResponse.getProductName())
                .productId(productResponse.getProductId())
                .build();

        OrderResponse.PaymentDetails paymentDetails
                = OrderResponse.PaymentDetails
                .builder()
                .paymentId(paymentResponse.getPaymentId())
                .paymentStatus(paymentResponse.getStatus())
                .paymentDate(paymentResponse.getPaymentDate())
                .paymentMode(paymentResponse.getPaymentMode())
                .build();

        OrderResponse orderResponse
                = OrderResponse.builder()
                .orderId(order.getId())
                .orderStatus(order.getOrderStatus())
                .amount(order.getAmount())
                .orderDate(order.getOrderDate())
                .productDetails(productDetails)
                .paymentDetails(paymentDetails)
                .build();

        return orderResponse;
    }
    
}
