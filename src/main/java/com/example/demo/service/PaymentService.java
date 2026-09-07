package com.example.demo.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.model.Orders;
import com.example.demo.model.Payments;
import com.example.demo.repository.OrdersRepository;
import com.example.demo.repository.PaymentsRepository;
import com.example.demo.model.Order_item;
import com.example.demo.model.Products;
import com.example.demo.repository.OrderItemRepository;
import com.example.demo.repository.ProductRepository;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PaymentService {
	@Autowired
    private PaymentsRepository paymentsRepository;

    @Autowired
    private OrdersRepository ordersRepository; 

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private ProductRepository productRepository;
    public Payments createPayments(Integer order_id, String payment_method,
    		String payment_status, String transaction_id, //BigDecimal amount,
    		LocalDateTime start_at, LocalDateTime paid_at) {

        Orders orders = ordersRepository.findById(order_id)
                .orElseThrow(() -> new RuntimeException("找不到對應的訂單 ID: " + order_id));
   
        Payments payments = new Payments();
        payments.setOrders(orders); 
        payments.setPayment_method(payment_method);
        payments.setPayment_status(payment_status);
        payments.setTransaction_id(transaction_id);
        payments.setAmount(orders.getTotal_amount());
        payments.setStart_at(start_at);
        payments.setPaid_at(paid_at);

        return paymentsRepository.save(payments);
    }
    public Orders getOrderForCheckout(Integer order_id) {

        return ordersRepository.findById(order_id)
                .orElseThrow(() ->
                    new RuntimeException("找不到訂單：" + order_id));
    }

    public Payments startPayment(Integer order_id) {

        Orders order = ordersRepository.findById(order_id)
                .orElseThrow(() ->
                    new RuntimeException("找不到訂單：" + order_id));

        Optional<Payments> existingPayment =
                paymentsRepository.findPendingPayment(
                        order_id,
                        "PENDING"
                );

        if (existingPayment.isPresent()) {

            Payments payment = existingPayment.get();
            if (order.getPayment_expires_at() != null
                    && LocalDateTime.now()
                        .isBefore(order.getPayment_expires_at())) {

                return payment;
            }
            return expirePayment(payment.getPayment_id());
        }

        LocalDateTime startAt = LocalDateTime.now();

        Payments payment = new Payments();

        payment.setOrders(order);
        payment.setPayment_status("PENDING");
        payment.setAmount(order.getTotal_amount());
        payment.setStart_at(startAt);
        order.setPayment_expires_at(
                startAt.plusSeconds(30)
        );

        ordersRepository.save(order);

        return paymentsRepository.save(payment);
    }
    
    @Transactional
    public Payments expirePayment(Integer payment_id) {

        Payments payment = paymentsRepository.findById(payment_id)
                .orElseThrow(() ->
                    new RuntimeException(
                        "找不到付款紀錄：" + payment_id
                    )
                );

        Orders order = payment.getOrders();
        if ("SUCCESS".equals(payment.getPayment_status())) {
            return payment;
        }

        if ("EXPIRED".equals(payment.getPayment_status())) {
            return payment;
        }
        Order_item orderItem =
                orderItemRepository
                    .findByOrdersOrderId(order.getOrder_id())
                    .orElseThrow(() ->
                        new RuntimeException(
                            "找不到訂單明細，order_id = "
                            + order.getOrder_id()
                        )
                    );

        Integer reservedQuantity =
                order.getReserved_quantity();


        if (reservedQuantity == null) {
            reservedQuantity = 0;
        }

        Products product =
                orderItem.getProducts();


        if (product == null) {
            throw new RuntimeException(
                "訂單明細沒有對應的商品"
            );
        }
        product.setAvailable_capacity(
                product.getAvailable_capacity()
                        + reservedQuantity
        );

        payment.setPayment_status("EXPIRED");
        order.setOrder_status("UNPAID");

        productRepository.save(product);
        ordersRepository.save(order);

        return paymentsRepository.save(payment);
    }
    
    @Transactional
    public Payments confirmPayment(
            Integer payment_id,
            String payment_method) {

        Payments payment = paymentsRepository.findById(payment_id)
                .orElseThrow(() ->
                    new RuntimeException(
                        "找不到付款紀錄：" + payment_id
                    )
                );

        Orders order = payment.getOrders();

        LocalDateTime now = LocalDateTime.now();

        if (order.getPayment_expires_at() == null) {

            throw new RuntimeException(
                "付款期限不存在"
            );
        }

        if (!now.isBefore(order.getPayment_expires_at())) {

            return expirePayment(payment_id);
        }
        payment.setPayment_method(payment_method);
        payment.setPayment_status("SUCCESS");
        payment.setPaid_at(now);

        order.setOrder_status("PAID");


        ordersRepository.save(order);

        return paymentsRepository.save(payment);
    }
    
    public Optional<Payments> getPaymentsById(Integer payment_id ) {
        return paymentsRepository.findById(payment_id );
    }

    public List<Payments> getAllPayments() {
        return paymentsRepository.findAll();
    }

    public Payments updatePayments(Integer payment_id, Integer order_id,
    		String payment_method, String payment_status, String transaction_id,
    		BigDecimal amount, LocalDateTime start_at, LocalDateTime paid_at) {
        
        Optional<Payments> existingPayments = paymentsRepository.findById(payment_id);

        if (existingPayments.isPresent()) {
        		Payments payments = existingPayments.get();
            Orders orders = ordersRepository.findById(order_id)
                    .orElseThrow(() -> new RuntimeException("找不到對應的訂單 ID: " + order_id));

            payments.setOrders(orders); 
            payments.setPayment_method(payment_method);
            payments.setPayment_status(payment_status);
            payments.setTransaction_id(transaction_id);
            payments.setAmount(amount);
            payments.setStart_at(start_at);
            payments.setPaid_at(paid_at);

            return paymentsRepository.save(payments);
        }

        throw new RuntimeException("此筆payment不存在: " + payment_id); 
    }

    public boolean deletePayments(Integer payment_id) {
        Optional<Payments> payment = paymentsRepository.findById(payment_id);
        if (payment.isPresent()) {
        		paymentsRepository.deleteById(payment_id);
            return true;
        }
        return false;
    }

    public long getPaymentsCount() {
        return paymentsRepository.count();
    }
}
