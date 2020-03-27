package upcafe.repository.orders;

import org.springframework.data.jpa.repository.JpaRepository;

import upcafe.entity.orders.Orders;

public interface OrderRepository extends JpaRepository<Orders, String>{

}
