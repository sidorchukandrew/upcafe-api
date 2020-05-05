package upcafe.entity.orders;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import upcafe.entity.signin.Customer;

@Entity
public class Payment {
	
	@Id
	@Column(length = 36)
	private String id;
	
	@OneToOne()
	@JoinColumn(name = "order_id", referencedColumnName = "id")
	private Orders order;
	private String paymentMadeAt;
	private double totalPaid;
	private String receiptUrl;
	
	@ManyToOne
	@JoinColumn(name = "customer_id", referencedColumnName = "id")
	private Customer customer;
	
	@Column(length = 15)
	private String status;

	public static class Builder {
		private final String id;
		private Orders order;
		private String paymentMadeAt;
		private double totalPaid;
		private String receiptUrl;
		private Customer customer;
		private String status;

		public Builder(String id) {
			this.id = id;
		}

		public Builder order(Orders order) {
			this.order = order;
			return this;
		}

		public Builder paymentMadeAt(String paymentMadeAt) {
			this.paymentMadeAt = paymentMadeAt;
			return this;
		}

		public Builder totalPaid(double totalPaid) {
			this.totalPaid = totalPaid;
			return this;
		}

		public Builder receiptUrl(String receiptUrl) {
			this.receiptUrl = receiptUrl;
			return this;
		}

		public Builder customer(Customer customer) {
			this.customer = customer;
			return this;
		}

		public Builder status(String status) {
			this.status = status;
			return this;
		}

		public Payment build() {
			return new Payment(this);
		}

	}

	public Payment(Builder builder) {
		this.id = builder.id;
		this.order = builder.order;
		this.paymentMadeAt = builder.paymentMadeAt;
		this.totalPaid = builder.totalPaid;
		this.receiptUrl = builder.receiptUrl;
		this.customer = builder.customer;
		this.status = builder.status;
	}

	public Payment() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Orders getOrder() {
		return order;
	}

	public void setOrder(Orders order) {
		this.order = order;
	}

	public String getPaymentMadeAt() {
		return paymentMadeAt;
	}

	public void setPaymentMadeAt(String paymentMadeAt) {
		this.paymentMadeAt = paymentMadeAt;
	}

	public double getTotalPaid() {
		return totalPaid;
	}

	public void setTotalPaid(double totalPaid) {
		this.totalPaid = totalPaid;
	}

	public String getReceiptUrl() {
		return receiptUrl;
	}

	public void setReceiptUrl(String receiptUrl) {
		this.receiptUrl = receiptUrl;
	}

	
	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "Payment [id=" + id + ", order=" + order + ", paymentMadeAt=" + paymentMadeAt + ", totalPaid="
				+ totalPaid + ", receiptUrl=" + receiptUrl + ", customer=" + customer + ", status=" + status + "]";
	}
}
