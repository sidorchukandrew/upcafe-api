package upcafe.model.orders;

public class PaymentData {

	private String nonce;
	private String orderId;
	private double price;
	
	public PaymentData(String nonce, String orderId, double price) {
		super();
		this.nonce = nonce;
		this.orderId = orderId;
		this.price = price;
	}
	
	public PaymentData() {
		super();
	}
	
	public String getNonce() {
		return nonce;
	}
	
	public void setNonce(String nonce) {
		this.nonce = nonce;
	}
	
	public String getOrderId() {
		return orderId;
	}
	
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	@Override
	public String toString() {
		return "PaymentData [nonce=" + nonce + ", orderId=" + orderId + ", price=" + price + "]";
	}
}
