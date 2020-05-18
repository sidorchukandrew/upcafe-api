package upcafe.entity.settings;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class PickupSettings {
	
	@Id
	@Column(length = 1)
	private String id;
	private int intervalBetweenPickupTimes;
	private Boolean pickupOnOpen;
	private Boolean pickupOnClose;
	
	private PickupSettings(Builder builder) {
		this.id = builder.id;
		this.intervalBetweenPickupTimes = builder.intervalBetweenPickupTimes;
		this.pickupOnOpen = builder.pickupOnOpen;
		this.pickupOnClose = builder.pickupOnClose;
	}

	public static class Builder {
		private final String id;
		private int intervalBetweenPickupTimes;
		private Boolean pickupOnOpen;
		private Boolean pickupOnClose;

		public Builder(String id) {
			this.id = id;
		}

		public Builder intervalBetweenPickupTimes(int intervalBetweenPickupTimes) {
			this.intervalBetweenPickupTimes = intervalBetweenPickupTimes;
			return this;
		}

		public Builder pickupOnOpen(Boolean pickupOnOpen) {
			this.pickupOnOpen = pickupOnOpen;
			return this;
		}

		public Builder pickupOnClose(Boolean pickupOnClose) {
			this.pickupOnClose = pickupOnClose;
			return this;
		}

		public PickupSettings build() {
			return new PickupSettings(this);
		}
	}

	public PickupSettings() {
		super();
	}

	public int getIntervalBetweenPickupTimes() {
		return intervalBetweenPickupTimes;
	}

	public void setIntervalBetweenPickupTimes(int interval) {
		this.intervalBetweenPickupTimes = interval;
	}

	public Boolean isPickupOnOpen() {
		return pickupOnOpen;
	}

	public void setPickupOnOpen(boolean pickupOnOpen) {
		this.pickupOnOpen = pickupOnOpen;
	}

	public Boolean isPickupOnClose() {
		return pickupOnClose;
	}

	public void setPickupOnClose(boolean pickupOnClose) {
		this.pickupOnClose = pickupOnClose;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "PickupSettings [id=" + id + ", interval=" + intervalBetweenPickupTimes + ", pickupOnOpen=" + pickupOnOpen
				+ ", pickupOnClose=" + pickupOnClose + "]";
	}
}
