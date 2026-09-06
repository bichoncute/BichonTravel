package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.example.demo.model.Payments;
@Repository
public interface PaymentsRepository extends JpaRepository<Payments, Integer> {

    @Query("""
        SELECT p
        FROM Payments p
        WHERE p.orders.order_id = :order_id
    """)
    List<Payments> findByOrderId(
            @Param("order_id") Integer order_id
    );

    @Query("""
        SELECT p
        FROM Payments p
        WHERE p.orders.order_id = :order_id
          AND p.payment_status = :payment_status
    """)
    Optional<Payments> findPendingPayment(
            @Param("order_id") Integer order_id,
            @Param("payment_status") String payment_status
    );
}
