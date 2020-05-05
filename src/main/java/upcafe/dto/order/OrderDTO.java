package upcafe.dto.order;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import upcafe.dto.users.CustomerDTO;

public class OrderDTO {
    
    private String id;
    private String status;
    private double totalPrice;

    @JsonFormat(pattern = "EEE MMM dd yyyy HH:mm")
    private LocalDateTime placedAt;

    @JsonFormat(pattern = "EEE MMM dd yyyy HH:mm")
    private LocalDateTime completedAt;
    private CustomerDTO customer;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime pickupTime;

    @JsonFormat(pattern = "EEE MMM dd yyyy")
    private LocalDate pickupDate;
    private List<OrderItemDTO> orderItems;

    public static class Builder {
        private String id;
        private String status;
        private double totalPrice;
        private LocalDateTime placedAt;
        private LocalDateTime completedAt;
        private CustomerDTO customer;
        private LocalTime pickupTime;
        private LocalDate pickupDate;
        private List<OrderItemDTO> orderItems;

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder status(String status) {
            this.status = status;
            return this;
        }

        public Builder totalPrice(double totalPrice) {
            this.totalPrice = totalPrice;
            return this;
        }

        public Builder placedAt(LocalDateTime placedAt) {
            this.placedAt = placedAt;
            return this;
        }

        public Builder completedAt(LocalDateTime completedAt) {
            this.completedAt = completedAt;
            return this;
        }

        public Builder customer(CustomerDTO customer) {
            this.customer = customer;
            return this;
        }

        public Builder pickupDate(LocalDate pickupDate) {
            this.pickupDate = pickupDate;
            return this;
        }

        public Builder pickupTime(LocalTime pickupTime) {
            this.pickupTime = pickupTime;
            return this;
        }

        public Builder orderItems(List<OrderItemDTO> orderItems) {
            this.orderItems = orderItems;
            return this;
        }

        public OrderDTO build() {
            return new OrderDTO(this);
        }
    }

    private OrderDTO(Builder builder) {
        this.id = builder.id;
        this.status = builder.status;
        this.totalPrice = builder.totalPrice;
        this.placedAt = builder.placedAt;
        this.pickupDate = builder.pickupDate;
        this.pickupTime = builder.pickupTime;
        this.completedAt = builder.completedAt;
        this.customer = builder.customer;
        this.orderItems = builder.orderItems;
    }

    public OrderDTO() { }
    
    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getTotalPrice() {
        return this.totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public LocalDateTime getPlacedAt() {
        return this.placedAt;
    }

    public void setPlacedAt(LocalDateTime placedAt) {
        this.placedAt = placedAt;
    }

    public LocalDateTime getCompletedAt() {
        return this.completedAt;
    }

    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }

    public CustomerDTO getCustomer() {
        return this.customer;
    }

    public void setCustomer(CustomerDTO customer) {
        this.customer = customer;
    }

    public LocalTime getPickupTime() {
        return this.pickupTime;
    }

    public void setPickupTime(LocalTime pickupTime) {
        this.pickupTime = pickupTime;
    }

    public LocalDate getPickupDate() {
        return this.pickupDate;
    }

    public void setPickupDate(LocalDate pickupDate) {
        this.pickupDate = pickupDate;
    }

    public List<OrderItemDTO> getOrderItems() {
        return this.orderItems;
    }

    public void setOrderItems(List<OrderItemDTO> orderItems) {
        this.orderItems = orderItems;
    }

    @Override
    public String toString() {
        return "{" +
            " id='" + id + "'" +
            ", status='" + status + "'" +
            ", totalPrice='" + totalPrice + "'" +
            ", placedAt='" + placedAt + "'" +
            ", completedAt='" + completedAt + "'" +
            ", customer='" + customer + "'" +
            ", pickupTime='" + pickupTime + "'" +
            ", pickupDate='" + pickupDate + "'" +
            ", orderItems='" + orderItems + "'" +
            "}";
    }
}