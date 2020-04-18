package upcafe.entity.settings;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class PickupSettings {
	
	@Id
	private String id;
	private int intervalBetweenPickupTimes;
	private Boolean pickupOnOpen;
	private Boolean pickupOnClose;
	
	public PickupSettings(String id, int intervalBetweenPickupTimes, boolean pickupOnOpen, boolean pickupOnClose) {
		super();
		this.id = id;
		this.intervalBetweenPickupTimes = intervalBetweenPickupTimes;
		this.pickupOnOpen = pickupOnOpen;
		this.pickupOnClose = pickupOnClose;
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
