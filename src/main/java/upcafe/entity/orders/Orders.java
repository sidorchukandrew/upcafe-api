package upcafe.entity.orders;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import upcafe.entity.signin.Customer;

@Entity
@Table(name = "Orders")
public class Orders {

	@Id
	@Column(name = "id")
	private String id;
	private String state;
	private double totalPrice;
	private String createdAt;
	private String closedAt;
	
	@ManyToOne
	@JoinColumn(name = "customer_id", referencedColumnName = "id")
	private Customer customer;
	
	public Orders(String id, double totalPrice, String state, String createdAt, String closedAt, Customer customer) {
		super();
		this.state = state;
		this.totalPrice = totalPrice;
		this.id = id;
		this.createdAt = createdAt;
		this.closedAt = closedAt;
		this.customer = customer;
	}

	public Orders() {
		super();
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public String getClosedAt() {
		return closedAt;
	}

	public void setClosedAt(String closedAt) {
		this.closedAt = closedAt;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	@Override
	public String toString() {
		return "Order [state=" + state + ", totalPrice=" + totalPrice + ", id=" + id + ", createdAt=" + createdAt
				+ ", closedAt=" + closedAt + ", customer=" + customer + "]";
	}
}
