package upcafe.repository.orders;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import upcafe.entity.orders.Orders;

public interface OrderRepository extends JpaRepository<Orders, String>{

	@Query("SELECT o FROM Orders o WHERE o.customer.id = :id AND o.state != 'COMPLETE'")
	public List<Orders> getOpenOrdersByCustomerId(@Param("id") int customerId);
}
