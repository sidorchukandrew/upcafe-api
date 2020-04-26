package upcafe.entity.orders;

import java.sql.Time;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import upcafe.entity.signin.Customer;

//@Entity
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
	
	private String pickupTime;
	private String pickupDate;

	public Orders(String id, String state, double totalPrice, String createdAt, String closedAt, Customer customer,
			String pickupTime, String pickupDate) {
		super();
		this.id = id;
		this.state = state;
		this.totalPrice = totalPrice;
		this.createdAt = createdAt;
		this.closedAt = closedAt;
		this.customer = customer;
		this.pickupTime = pickupTime;
		this.pickupDate = pickupDate;
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

	public String getPickupTime() {
		return pickupTime;
	}

	public void setPickupTime(String pickupTime) {
		this.pickupTime = pickupTime;
	}
	
	public String getPickupDate() {
		return pickupDate;
	}

	public void setPickupDate(String pickupDate) {
		this.pickupDate = pickupDate;
	}

	@Override
	public String toString() {
		return "Orders [id=" + id + ", state=" + state + ", totalPrice=" + totalPrice + ", createdAt=" + createdAt
				+ ", closedAt=" + closedAt + ", customer=" + customer + ", pickupTime=" + pickupTime + ", pickupDate="
				+ pickupDate + "]";
	}
}
