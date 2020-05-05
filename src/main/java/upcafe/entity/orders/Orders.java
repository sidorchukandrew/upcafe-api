package upcafe.entity.orders;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

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
	@Column(length = 36)
	private String id;

	@Column(length = 15)
	private String status;
	private double totalPrice;
	private LocalDateTime placedAt;
	private LocalDateTime completedAt;

	@ManyToOne
	@JoinColumn(name = "customer_id", referencedColumnName = "id")
	private Customer customer;

	private LocalTime pickupTime;
	private LocalDate pickupDate;

	public static class Builder {
		private final String id;
		private String status;
		private double totalPrice;
		private LocalDateTime placedAt;
		private LocalDateTime completedAt;
		private Customer customer;
		private LocalTime pickupTime;
		private LocalDate pickupDate;

		public Builder(String id) {
			this.id = id;
		}

		public Builder status(String status) {
			this.status = status;
			return this;
		}

		public Builder totalPrice(double totalPrice) {
			this.totalPrice = totalPrice;
			return this;
		}

		public Builder placedAt(LocalDateTime placedAt) {
			this.placedAt = placedAt;
			return this;
		}

		public Builder completedAt(LocalDateTime completedAt) {
			this.completedAt = completedAt;
			return this;
		}

		public Builder customer(Customer customer) {
			this.customer = customer;
			return this;
		}

		public Builder pickupTime(LocalTime pickupTime) {
			this.pickupTime = pickupTime;
			return this;
		}

		public Builder pickupDate(LocalDate pickupDate) {
			this.pickupDate = pickupDate;
			return this;
		}

		public Orders build() {
			return new Orders(this);
		}
	}

	public Orders(Builder builder) {
		this.id = builder.id;
		this.status = builder.status;
		this.totalPrice = builder.totalPrice;
		this.placedAt = builder.placedAt;
		this.completedAt = builder.completedAt;
		this.customer = builder.customer;
		this.pickupTime = builder.pickupTime;
		this.pickupDate = builder.pickupDate;
	}

	public Orders() {
		super();
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public LocalDateTime getPlacedAt() {
		return placedAt;
	}

	public void setPlacedAt(LocalDateTime placedAt) {
		this.placedAt = placedAt;
	}

	public LocalDateTime getCompletedAt() {
		return completedAt;
	}

	public void setCompletedAt(LocalDateTime completedAt) {
		this.completedAt = completedAt;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public LocalTime getPickupTime() {
		return pickupTime;
	}

	public void setPickupTime(LocalTime pickupTime) {
		this.pickupTime = pickupTime;
	}

	public LocalDate getPickupDate() {
		return pickupDate;
	}

	public void setPickupDate(LocalDate pickupDate) {
		this.pickupDate = pickupDate;
	}

	@Override
	public String toString() {
		return "{" +
			" id='" + id + "'" +
			", status='" + status + "'" +
			", totalPrice='" + totalPrice + "'" +
			", placedAt='" + placedAt + "'" +
			", completedAt='" + completedAt + "'" +
			", customer='" + customer + "'" +
			", pickupTime='" + pickupTime + "'" +
			", pickupDate='" + pickupDate + "'" +
			"}";
	}
}
