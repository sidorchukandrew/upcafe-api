package upcafe.repository.orders;

import org.springframework.data.jpa.repository.JpaRepository;

import upcafe.entity.orders.Payment;

public interface PaymentRepository extends JpaRepository<Payment, String> {

}
