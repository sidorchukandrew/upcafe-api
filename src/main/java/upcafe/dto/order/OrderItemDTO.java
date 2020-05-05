package upcafe.dto.order;

import java.util.List;

public class OrderItemDTO {
    
    private String variationId;
    private String name;
    private double price;
    private List<OrderModifierDTO> selectedModifiers;
    private int quantity;

    public static class Builder {
        private String variationId;
        private String name;
        private double price;
        private List<OrderModifierDTO> selectedModifiers;
        private int quantity;

        public Builder(String variationId) {
            this.variationId = variationId;
        }

        public Builder variationId(String variationId) {
            this.variationId = variationId;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder price(double price) {
            this.price = price;
            return this;
        }

        public Builder selectedModifiers(List<OrderModifierDTO> selectedModifiers) {
            this.selectedModifiers = selectedModifiers;
            return this;
        }

        public Builder quantity(int quantity) {
            this.quantity = quantity;
            return this;
        }

        public OrderItemDTO build() {
            return new OrderItemDTO(this);
        }
    }

    private OrderItemDTO(Builder builder) {
        this.variationId = builder.variationId;
        this.name = builder.name;
        this.price = builder.price;
        this.selectedModifiers = builder.selectedModifiers;
        this.quantity = builder.quantity;
    }

    public OrderItemDTO() {
        
    }


    public String getVariationId() {
        return this.variationId;
    }

    public void setVariationId(String variationId) {
        this.variationId = variationId;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return this.price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public List<OrderModifierDTO> getSelectedModifiers() {
        return this.selectedModifiers;
    }

    public void setSelectedModifiers(List<OrderModifierDTO> selectedModifiers) {
        this.selectedModifiers = selectedModifiers;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "{" +
            " variationId='" + variationId + "'" +
            ", name='" + name + "'" +
            ", price='" + price + "'" +
            ", selectedModifiers='" + selectedModifiers + "'" +
            ", quantity='" + quantity + "'" +
            "}";
    }

}