package upcafe.dto.settings;

public class PickupSettingsDTO {

    private String id;
    private int intervalBetweenPickupTimes;
    private Boolean pickupOnOpen;
    private Boolean pickupOnClose;

    public PickupSettingsDTO() {
    }

    public static class Builder {
        private String id;
        private int intervalBetweenPickupTimes;
        private Boolean pickupOnOpen;
        private Boolean pickupOnClose;


        public Builder id(String id) {
            this.id = id;
            return this;
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

        public PickupSettingsDTO build() {
            return new PickupSettingsDTO(this);
        }

    }

    private PickupSettingsDTO(Builder builder) {
        this.id = builder.id;
        this.intervalBetweenPickupTimes = builder.intervalBetweenPickupTimes;
        this.pickupOnClose = builder.pickupOnClose;
        this.pickupOnOpen = builder.pickupOnOpen;
    }


    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getIntervalBetweenPickupTimes() {
        return this.intervalBetweenPickupTimes;
    }

    public void setIntervalBetweenPickupTimes(int intervalBetweenPickupTimes) {
        this.intervalBetweenPickupTimes = intervalBetweenPickupTimes;
    }

    public Boolean isPickupOnOpen() {
        return this.pickupOnOpen;
    }

    public Boolean getPickupOnOpen() {
        return this.pickupOnOpen;
    }

    public void setPickupOnOpen(Boolean pickupOnOpen) {
        this.pickupOnOpen = pickupOnOpen;
    }

    public Boolean isPickupOnClose() {
        return this.pickupOnClose;
    }

    public Boolean getPickupOnClose() {
        return this.pickupOnClose;
    }

    public void setPickupOnClose(Boolean pickupOnClose) {
        this.pickupOnClose = pickupOnClose;
    }

    @Override
    public String toString() {
        return "{" +
                " id='" + id + "'" +
                ", intervalBetweenPickupTimes='" + intervalBetweenPickupTimes + "'" +
                ", pickupOnOpen='" + pickupOnOpen + "'" +
                ", pickupOnClose='" + pickupOnClose + "'" +
                "}";
    }
}