package upcafe.model.orders;

public class OrderConfirmation {

	private String id;
	private String createdAt;
	private String closedAt;
	private String state;
	private double totalPrice;
	
	public OrderConfirmation(String id, String createdAt, String closedAt, String state, double totalPrice) {
		super();
		this.id = id;
		this.createdAt = createdAt;
		this.closedAt = closedAt;
		this.state = state;
		this.totalPrice = totalPrice;
	}

	public OrderConfirmation() {
		super();
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

	@Override
	public String toString() {
		return "OrderConfirmation [id=" + id + ", createdAt=" + createdAt + ", closedAt=" + closedAt + ", state="
				+ state + ", totalPrice=" + totalPrice + "]";
	}
}
