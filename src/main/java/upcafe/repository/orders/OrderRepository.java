package upcafe.repository.orders;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import upcafe.entity.orders.Orders;

public interface OrderRepository extends JpaRepository<Orders, String>{

	// @Query("SELECT o FROM Orders o WHERE o.customer.id = :id AND o.state != 'COMPLETE' And o.state != 'CANCELLED'")
	// public Orders getActiveOrdersByCustomerId(@Param("id") int customerId);
	
	public List<Orders> getOrdersByStatus(String status);
	
	public List<Orders> getOrdersByPickupDate(LocalDate date);
}
