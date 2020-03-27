package upcafe.model.orders;

public class PaymentConfirmation {
	
	private String id;
	private String orderId;
	private String paymentMadeAt;
	private double totalPaid;
	private String receiptUrl;
	
	public PaymentConfirmation(String id, String paymentMadeAt, double totalPaid, String receiptUrl, String orderId) {
		super();
		this.id = id;
		this.paymentMadeAt = paymentMadeAt;
		this.totalPaid = totalPaid;
		this.receiptUrl = receiptUrl;
		this.orderId = orderId;
	}
	
	public PaymentConfirmation() {
		
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
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
	
	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	@Override
	public String toString() {
		return "PaymentConfirmation [id=" + id + ", orderId=" + orderId + ", paymentMadeAt=" + paymentMadeAt
				+ ", totalPaid=" + totalPaid + ", receiptUrl=" + receiptUrl + "]";
	}

}
