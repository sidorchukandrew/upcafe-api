package upcafe.entity.orders;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import upcafe.entity.signin.Customer;

//@Entity
public class Payment {
	
	@Id
	private String id;
	
	@OneToOne(cascade = {CascadeType.ALL})
	@JoinColumn(name = "order_id", referencedColumnName = "id")
	private Orders order;
	private String paymentMadeAt;
	private double totalPaid;
	private String receiptUrl;
	
	@ManyToOne
	@JoinColumn(name = "customer_id", referencedColumnName = "id")
	private Customer customer;
	
	private String status;

	public Payment(String id, Orders order, String paymentMadeAt, double totalPaid, String receiptUrl,
			Customer customer, String status) {
		super();
		this.id = id;
		this.order = order;
		this.paymentMadeAt = paymentMadeAt;
		this.totalPaid = totalPaid;
		this.receiptUrl = receiptUrl;
		this.customer = customer;
		this.status = status;
	}

	public Payment() {
		super();
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
