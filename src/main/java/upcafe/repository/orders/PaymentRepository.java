package upcafe.repository.orders;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Modifying;
import upcafe.entity.orders.Payment;

import javax.transaction.Transactional;

public interface PaymentRepository extends JpaRepository<Payment, String> {

    @Modifying
    @Transactional
    void deleteByOrderId(String id);
}
