package upcafe.v2.models;

import upcafe.v2.enums.PickupState;

import java.time.LocalDateTime;

public class PickupDetails {
    private LocalDateTime pickupTimeRequested;
    private PickupState state;
    private String fulfillmentId;

    public PickupDetails() { }

    public LocalDateTime getPickupTimeRequested() {
        return pickupTimeRequested;
    }

    public PickupState getState() {
        return state;
    }

    public String getFulfillmentId() {
        return fulfillmentId;
    }

    public static class Builder {
        private LocalDateTime pickupTimeRequested;
        private PickupState state;
        private String fulfillmentId;

        public Builder fulfillmentId(String fulfillmentId) {
            this.fulfillmentId = fulfillmentId;
            return this;
        }

        public Builder pickupTimeRequested(LocalDateTime pickupTimeRequested) {
            this.pickupTimeRequested = pickupTimeRequested;
            return this;
        }

        public Builder state(PickupState state) {
            this.state = state;
            return this;
        }

        public PickupDetails build() {
            return new PickupDetails(this);
        }
    }

    private PickupDetails(Builder builder) {
        this.fulfillmentId = builder.fulfillmentId;
        this.pickupTimeRequested = builder.pickupTimeRequested;
        this.state = builder.state;
    }

    @Override
    public String toString() {
        return "PickupDetails{" +
                "pickupTimeRequested=" + pickupTimeRequested +
                ", state=" + state +
                ", fulfillmentId='" + fulfillmentId + '\'' +
                '}';
    }
}
