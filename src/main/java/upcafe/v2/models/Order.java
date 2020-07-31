package upcafe.v2.models;

import java.time.LocalDateTime;
import java.util.List;

public class Order {

    private Customer customer;
    private List<OrderItem> orderItems;
    private PickupDetails pickupDetails;
    private Costs costs;
    private String id;
    private LocalDateTime placedAt;
    private LocalDateTime startedAt;
    private LocalDateTime readyAt;
    private LocalDateTime pickedUpAt;
    private int version;

    public Order() { }

    public static class Builder {
        private Customer customer;
        private List<OrderItem> orderItems;
        private PickupDetails pickupDetails;
        private Costs costs;
        private final String id;
        private LocalDateTime placedAt;
        private LocalDateTime startedAt;
        private LocalDateTime readyAt;
        private LocalDateTime pickedUpAt;
        private int version;

        public Builder(String orderId) {
            this.id = orderId;
        }

        public Builder customer(Customer customer) {
            this.customer = customer;
            return this;
        }

        public Builder pickupDetails(PickupDetails pickupDetails) {
            this.pickupDetails = pickupDetails;
            return this;
        }

        public Builder costs(Costs costs) {
            this.costs = costs;
            return this;
        }

        public Builder placedAt(LocalDateTime placedAt) {
            this.placedAt = placedAt;
            return this;
        }

        public Builder startedAt(LocalDateTime startedAt) {
            this.startedAt = startedAt;
            return this;
        }

        public Builder readyAt(LocalDateTime readyAt) {
            this.readyAt = readyAt;
            return this;
        }

        public Builder pickedUpAt(LocalDateTime pickedUpAt) {
            this.pickedUpAt = pickedUpAt;
            return this;
        }

        public Builder orderItems(List<OrderItem> orderItems) {
            this.orderItems = orderItems;
            return this;
        }

        public Builder version(int version) {
            this.version = version;
            return this;
        }

        public Order build() {
            return new Order(this);
        }
    }

    private Order(Builder builder) {
        this.costs = builder.costs;
        this.customer = builder.customer;
        this.id = builder.id;
        this.orderItems = builder.orderItems;
        this.pickedUpAt = builder.pickedUpAt;
        this.pickupDetails = builder.pickupDetails;
        this.placedAt = builder.placedAt;
        this.readyAt = builder.readyAt;
        this.startedAt = builder.startedAt;
        this.version = builder.version;
    }

    public Costs getCosts() {
        return costs;
    }

    public Customer getCustomer() {
        return customer;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public LocalDateTime getPickedUpAt() {
        return pickedUpAt;
    }

    public LocalDateTime getPlacedAt() {
        return placedAt;
    }

    public LocalDateTime getReadyAt() {
        return readyAt;
    }

    public LocalDateTime getStartedAt() {
        return startedAt;
    }

    public PickupDetails getPickupDetails() {
        return pickupDetails;
    }

    public String getId() {
        return id;
    }

    public int getVersion() {
        return version;
    }

    @Override
    public String toString() {
        return "Order{" +
                "customer=" + customer +
                ", orderItems=" + orderItems +
                ", pickupDetails=" + pickupDetails +
                ", costs=" + costs +
                ", id='" + id + '\'' +
                ", placedAt=" + placedAt +
                ", startedAt=" + startedAt +
                ", readyAt=" + readyAt +
                ", pickedUpAt=" + pickedUpAt +
                ", version=" + version +
                '}';
    }
}
