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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + intervalBetweenPickupTimes;
		result = prime * result + ((pickupOnClose == null) ? 0 : pickupOnClose.hashCode());
		result = prime * result + ((pickupOnOpen == null) ? 0 : pickupOnOpen.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PickupSettings other = (PickupSettings) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (intervalBetweenPickupTimes != other.intervalBetweenPickupTimes)
			return false;
		if (pickupOnClose == null) {
			if (other.pickupOnClose != null)
				return false;
		} else if (!pickupOnClose.equals(other.pickupOnClose))
			return false;
		if (pickupOnOpen == null) {
			if (other.pickupOnOpen != null)
				return false;
		} else if (!pickupOnOpen.equals(other.pickupOnOpen))
			return false;
		return true;
	}
    
    
}
