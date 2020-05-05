package upcafe.repository.orders;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import upcafe.entity.orders.Orders;

public interface OrderRepository extends JpaRepository<Orders, String>{

	@Query("SELECT o FROM Orders o WHERE o.customer.id = :id AND o.status != 'COMPLETE' And o.status != 'CANCELLED'")
	public Orders getActiveOrdersByCustomerId(@Param("id") int customerId);
	
	public List<Orders> getOrdersByStatus(String status);
	
	public List<Orders> getOrdersByPickupDate(LocalDate date);

	@Modifying
	@Transactional
	@Query("UPDATE Orders o SET o.status = :status WHERE o.id = :id")
	public void updateStatusForOrderWithId(@Param("id") String id, @Param("status") String status);
}
